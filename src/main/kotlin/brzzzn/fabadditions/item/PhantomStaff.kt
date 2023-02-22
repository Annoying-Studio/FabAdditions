package brzzzn.fabadditions.item

import brzzzn.fabadditions.Constants
import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.guis.PhantomStaffGui
import brzzzn.fabadditions.screens.PhantomStaffScreen
import kotlinx.coroutines.*
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class PhantomStaff(settings: Settings) : Item(settings) {

    private var phantomStaffScreen: Screen? = null
    private var teleportJob: Job? = null

    init {
        when (FabricLoader.getInstance().environmentType) {
            EnvType.CLIENT -> {
                // Client logic in response to server packet
                ClientPlayNetworking.registerGlobalReceiver(Constants.NetworkChannel.PHANTOM_STAFF_S2C_PACKET_ID) {
                        client, _, _, _ ->
                    client.execute {
                        // Get players from server
                        val players = hashSetOf<PlayerEntity>()

                        client.server?.worlds?.forEach {
                            players.addAll(it.players)
                        }

                        // Remove self from set
                        players.removeIf {
                            it.uuid == client.player?.uuid
                        }

                        // Create display screen
                        phantomStaffScreen = PhantomStaffScreen(PhantomStaffGui(players) {
                            ClientPlayNetworking.send(
                                Constants.NetworkChannel.PHANTOM_STAFF_C2S_PACKET_ID,
                                PacketByteBufs.create().writeUuid(it.uuid)
                            )
                            phantomStaffScreen?.close()
                        })

                        client.setScreen(phantomStaffScreen)
                    }
                }
            }

            else -> { }
        }

        // Server logic in response to client packet
        ServerPlayNetworking.registerGlobalReceiver(Constants.NetworkChannel.PHANTOM_STAFF_C2S_PACKET_ID) {
                server, client, _, buf, _ ->

            val sourcePlayer = server.playerManager.getPlayer(client.uuid) ?: run {
                FabAdditions.logger.warn("Sending Player found with uuid: ${buf.readUuid()}")
                return@registerGlobalReceiver
            }

            val targetPlayer = server.playerManager.getPlayer(buf.readUuid()) ?: run {
                FabAdditions.logger.warn("Target Player found with uuid: ${buf.readUuid()}")
                return@registerGlobalReceiver
            }

            teleportToPlayer(
                sourcePlayer,
                targetPlayer
            )
        }
    }
    // Handle use -> Only handle on server
    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        if (world?.isClient() == true || hand != Hand.MAIN_HAND || user?.isSneaking == true) return run {
            FabAdditions.logger.trace("Is Client. Server must issue action")
            super.use(world, user, hand)
        }

        // Check that player is server player
        if (user !is ServerPlayerEntity) return super.use(world, user, hand)

        ServerPlayNetworking.send(
            user,
            Constants.NetworkChannel.PHANTOM_STAFF_S2C_PACKET_ID,
            PacketByteBufs.empty()
        )

        return super.use(world, user, hand)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun teleportToPlayer(user: PlayerEntity, player: PlayerEntity) {
        if (user !is ServerPlayerEntity) return

        // Add effects
        user.addStatusEffect(
            StatusEffectInstance(StatusEffects.LEVITATION, 3*20, 1, false, false)
        )

        user.addStatusEffect(
            StatusEffectInstance(StatusEffects.NAUSEA, 8*20, 3, false, false)
        )

        user.addStatusEffect(
            StatusEffectInstance(StatusEffects.BLINDNESS, 4*20, 1, false, false)
        )

        // Start coroutine to teleport player after a delay
        if (teleportJob?.isActive == true) return
        teleportJob = GlobalScope.launch {
            delay(3000)
            user.server.getWorld(player.world.registryKey)
                ?.playSound(null, user.blockPos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.5f, 1f)

            user.teleport(user.server.getWorld(player.world.registryKey), player.x, player.y, player.z, player.headYaw, 0.5f)
        }

        user.itemCooldownManager[this] = 120
    }

    override fun appendTooltip(stack: ItemStack?, world: World?, tooltip: MutableList<Text>?, context: TooltipContext?) {
        if (Screen.hasShiftDown()) {
            tooltip?.add(Text.translatable("item.fabadditions.phantom_staff.tooltip").formatted(Formatting.GRAY))
        } else {
            tooltip?.add(Text.translatable("global.fabadditions.tooltip").formatted(Formatting.GRAY))
        }
        super.appendTooltip(stack, world, tooltip, context)
    }
}