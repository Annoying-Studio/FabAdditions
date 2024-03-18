package brzzzn.fabadditions.item.phantomstaff

import brzzzn.fabadditions.Constants
import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.data.PlayerList
import brzzzn.fabadditions.data.PlayerRef
import brzzzn.fabadditions.ui.guis.phantomstaff.PhantomStaffGui
import brzzzn.fabadditions.ui.screens.FabAdditionsUiScreen
import com.google.gson.Gson
import kotlinx.coroutines.*
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.*
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import java.util.*

class PhantomStaff(settings: Settings) : Item(settings) {

    private var phantomStaffScreen: Screen? = null
    private var teleportJob: Job? = null


    companion object {
        // Allows the player to teleport to himself -> only for debug purposes
        private const val ALLOW_TELEPORT_TO_SELF = false
        const val EXPERIENCE_COST: Int = 1
        const val COOLDOWN: Int = 240
    }

    init {
        when (FabricLoader.getInstance().environmentType) {
            EnvType.CLIENT -> {
                setupClientNetworkPackets()
            }

            else -> { }
        }

        // Always register to work on servers and in single-player
        setupServerNetworkPackets()
    }

    /**
     * Use code. Run by the server as soon as a player uses the item
     */
    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        if (world?.isClient() == true || hand != Hand.MAIN_HAND || user?.isSneaking == true) {
            FabAdditions.logger.trace("Is Client. Server must issue action")
            return super.use(world, user, hand)
        }

        // Check that player is server player
        if (user !is ServerPlayerEntity) return super.use(world, user, hand)

        // stop if the player does not have enough xp
        if(user.experienceLevel < EXPERIENCE_COST)
        {
            user.sendMessage(Text.translatable("chat.fabadditions.not_enough_xp").formatted(Formatting.RED))
            return super.use(world, user, hand)
        }

        val players = PlayerList(mutableListOf())

        for (worldInstance in world?.server?.worlds ?: listOf()) {
            players.players.addAll(
                worldInstance.players.map {
                    PlayerRef(it.displayName.string, it.uuid.toString())
                }
            )
        }

        // Filter out own player from list
        if (!ALLOW_TELEPORT_TO_SELF) {
            players.players.removeIf {
                it.uuid == user.uuidAsString
            }
        }

        val string = Gson().toJson(players)

        ServerPlayNetworking.send(
            user,
            Constants.NetworkChannel.PhantomStaff.S2C_ITEM_USAGE_PACKET_ID,
            PacketByteBufs.create().writeString(string)
        )

        return super.use(world, user, hand)
    }

    private fun setupClientNetworkPackets() {
        // Client logic in response to server packet
        ClientPlayNetworking.registerGlobalReceiver(
            Constants.NetworkChannel.PhantomStaff.S2C_ITEM_USAGE_PACKET_ID
        ) { client, _, buf, _ ->
            onReceivedItemUsageFromServer(client, buf.readString())
        }
    }

    private fun setupServerNetworkPackets() {
        // Server logic in response to client packet
        ServerPlayNetworking.registerGlobalReceiver(
            Constants.NetworkChannel.PhantomStaff.C2S_TARGET_PLAYER_SELECTED_PACKET_ID
        ) { server, client, _, buf, _ ->
            onReceivedPlayerSelectFromClient(server, client, buf.readString())
        }
    }

    /**
     * Client code that runs as soon as the server recognizes the usage of an item
     */
    private fun onReceivedItemUsageFromServer(client: MinecraftClient, receivedString: String) {
        FabAdditions.logger.trace(receivedString)
        val players = Gson().fromJson(receivedString, PlayerList::class.java)

        FabAdditions.logger.debug(players.toString())

        // Create display screen
        phantomStaffScreen = FabAdditionsUiScreen(PhantomStaffGui(players) {
            // Code that runs when the player has selected another player
            ClientPlayNetworking.send(
                Constants.NetworkChannel.PhantomStaff.C2S_TARGET_PLAYER_SELECTED_PACKET_ID,
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

    /**
     * Server code that runs as soon as a player has selected a target to teleport to
     */
    private fun onReceivedPlayerSelectFromClient(server: MinecraftServer, client: ServerPlayerEntity, receivedString: String) {
        try {
            val uuid: UUID = UUID.fromString(receivedString)

            val sourcePlayer = server.playerManager.getPlayer(client.uuid) ?: run {
                FabAdditions.logger.warn("No sending Player found with uuid: $uuid")
                return
            }

            val targetPlayer = server.playerManager.getPlayer(uuid) ?: run {
                FabAdditions.logger.warn("No target Player found with uuid: $uuid")
                return
            }

            // Enqueue Teleport request
            TeleportQueue.enqueueRequest(PlayerRef(sourcePlayer), PlayerRef(targetPlayer))

            // Send chat message
            sendChatMessageOnTargetPlayer(sourcePlayer, targetPlayer)

            // Set cool down
            sourcePlayer.itemCooldownManager[this] = COOLDOWN

        } catch (e: Exception) {
            FabAdditions.logger.error(
                "Encountered an error: ${e.message} on serverside while trying to teleport player: ${client.name} : ${client.uuid}", e
            )
        }
    }

    /**
     * Sends a chat request for allowing the Teleport (from [user] to [player]) to the [player]
     * + Enqueues the [user] for teleporting to [player]
     *
     * @param user The [PlayerEntity] that wants to teleport to the [player]
     * @param player The target of the teleport
     */
    private fun sendChatMessageOnTargetPlayer(user:PlayerEntity, player: PlayerEntity) {
        player.sendMessage(
            Text.translatable("item.fabadditions.phantom_staff.message", user.name.string)
        )
        player.sendMessage(
            Text.translatable("item.fabadditions.phantom_staff.message.accept")
                .setStyle(
                    Style.EMPTY
                        .withBold(true)
                        .withColor(
                            TextColor.fromFormatting(Formatting.GREEN)
                        )
                        .withHoverEvent(
                            HoverEvent.Action.SHOW_TEXT.buildHoverEvent(
                                Text.translatable("item.fabadditions.phantom_staff.message.accept.hover")
                            )
                        )
                        .withClickEvent(
                            ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/phantomtpaccept"
                            ),
                        )
                )
        )
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