package brzzzn.fabadditions.item

import brzzzn.fabadditions.Constants
import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.data.PlayerList
import brzzzn.fabadditions.data.PlayerRef
import brzzzn.fabadditions.guis.PhantomStaffGui
import brzzzn.fabadditions.screens.PhantomStaffScreen
import com.google.gson.Gson
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
import java.util.*

class PhantomStaff(settings: Settings) : Item(settings) {

    private var phantomStaffScreen: Screen? = null
    private var teleportJob: Job? = null

    init {
        when (FabricLoader.getInstance().environmentType) {
            EnvType.CLIENT -> {
                // Client logic in response to server packet
                ClientPlayNetworking.registerGlobalReceiver(Constants.NetworkChannel.PHANTOM_STAFF_S2C_PACKET_ID) {
                        client, _, buf, _ ->
                    val buffer = buf.readString()
                    FabAdditions.logger.debug(buffer)
                    val players = Gson().fromJson(buffer, PlayerList::class.java)

                    println(players)


                    // Create display screen
                    phantomStaffScreen = PhantomStaffScreen(PhantomStaffGui(players) {
                        ClientPlayNetworking.send(
                            Constants.NetworkChannel.PHANTOM_STAFF_C2S_PACKET_ID,
                            PacketByteBufs.create().writeString(it.uuid)
                        )
                        client.execute {
                            phantomStaffScreen?.close()
                        }
                    })
                    client.execute {
                        client.setScreen(phantomStaffScreen)
                    }
                }
            }

            else -> { }
        }

        // Server logic in response to client packet
        ServerPlayNetworking.registerGlobalReceiver(Constants.NetworkChannel.PHANTOM_STAFF_C2S_PACKET_ID) {
                server, client, _, buf, _ ->

            try {
                val string = buf.readString()

                val uuid: UUID = UUID.fromString(string)

                val sourcePlayer = server.playerManager.getPlayer(client.uuid) ?: run {
                    FabAdditions.logger.warn("Sending Player found with uuid: $uuid")
                    return@registerGlobalReceiver
                }

                val targetPlayer = server.playerManager.getPlayer(uuid) ?: run {
                    FabAdditions.logger.warn("Target Player found with uuid: $uuid")
                    return@registerGlobalReceiver
                }

                teleportToPlayer(
                    sourcePlayer,
                    targetPlayer
                )
            } catch (e: Exception) {
                FabAdditions.logger.error(
                    "Encountered an error: ${e.message} on serverside while trying to teleport player: ${client.name} : ${client.uuid}", e
                )
            }


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

        val players = PlayerList(mutableListOf())

        for (worldInstance in world?.server?.worlds ?: listOf()) {
            players.players.addAll(
                worldInstance.players.map {
                    PlayerRef(it.displayName.string, it.uuid.toString())
                }
            )
        }

        players.players.removeIf {
            it.uuid == user.uuidAsString
        }

        val string = Gson().toJson(players)

        ServerPlayNetworking.send(
            user,
            Constants.NetworkChannel.PHANTOM_STAFF_S2C_PACKET_ID,
            PacketByteBufs.create().writeString(string)
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
            tooltip?.add(Text.translatable("tooltip.fabadditions.hold_shift").formatted(Formatting.GRAY))
        }
        super.appendTooltip(stack, world, tooltip, context)
    }
}