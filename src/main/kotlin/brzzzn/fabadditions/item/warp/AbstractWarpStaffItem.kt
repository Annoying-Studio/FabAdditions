package brzzzn.fabadditions.item.warp

import brzzzn.fabadditions.Constants
import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.data.PlayerRef
import brzzzn.fabadditions.data.WarpPosition
import brzzzn.fabadditions.framework.Vector2
import brzzzn.fabadditions.framework.Vector3
import brzzzn.fabadditions.framework.WorldRef
import brzzzn.fabadditions.ui.guis.warpstaff.WarpGui
import brzzzn.fabadditions.item.warp.network.AddPosition
import brzzzn.fabadditions.item.warp.network.DeleteWarp
import brzzzn.fabadditions.item.warp.network.OpenStaff
import brzzzn.fabadditions.item.warp.network.RequestTeleport
import brzzzn.fabadditions.ui.screens.FabAdditionsUiScreen
import com.google.gson.Gson
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

abstract class AbstractWarpStaffItem(settings: Settings, private val warpType: WarpType) : Item(settings) {

    private var screen: Screen? = null

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

    private fun setupClientNetworkPackets() {
        // Client logic in response to server packet
        ClientPlayNetworking.registerGlobalReceiver(
            Constants.NetworkChannel.Warp.S2C_STAFF_ITEM_USAGE_PACKET_ID
        ) { client, _, buf, _ ->
            val obj = Gson().fromJson(buf.readString(), OpenStaff::class.java)
            onServerReportItemUsage(client, obj)
        }
    }

    private fun setupServerNetworkPackets() {
        // Server logic in response to client packet
        ServerPlayNetworking.registerGlobalReceiver(
            Constants.NetworkChannel.Warp.C2S_WARP_ADD_PACKET_ID
        ) { server, client, _, buf, _ ->
            val obj = Gson().fromJson(buf.readString(), AddPosition::class.java)
            onClientAddWarp(server, client, obj)
        }

        ServerPlayNetworking.registerGlobalReceiver(
            Constants.NetworkChannel.Warp.C2S_WARP_REQUEST_PACKET_ID
        ) { server, client, _, buf, _ ->
            val obj = Gson().fromJson(buf.readString(), RequestTeleport::class.java)
            onClientRequestWarp(server, client, obj)
        }

        ServerPlayNetworking.registerGlobalReceiver(
            Constants.NetworkChannel.Warp.C2S_WARP_DELETE_PACKET_ID
        ) { _, client, _, buf, _ ->
            val obj = Gson().fromJson(buf.readString(), DeleteWarp::class.java)
            onClientRequestDelete(client, obj)
        }
    }

    private fun onClientAddWarp(server: MinecraftServer, client: ServerPlayerEntity, addPosition: AddPosition) {
        WarpRepository.initializeCache(server)

        // Verify that player is not creating more teleports than necessary
        val playerWarps = WarpRepository.getWarpsFromPlayer(PlayerRef(client), addPosition.type)

        if (playerWarps.warps.size >= (WarpRepository.getWarpLimit(addPosition.type))) {
            FabAdditions.logger.info("Player: ${PlayerRef(client)} tried to request a new warp but has no teleports left")
            return
        }

        WarpRepository.createWarp(
            PlayerRef(client),
            WarpPosition(
                Vector3(client.pos),
                Vector2(client.rotationClient.x.toDouble(), client.rotationClient.y.toDouble()),
                addPosition.positionName,
                WorldRef(client.world.registryKey)
            ),
            addPosition.type
        )
    }

    private fun onClientRequestWarp(server: MinecraftServer, client: ServerPlayerEntity, request: RequestTeleport) {
        // FIXME: Validate teleport
        // TODO: Add effects

        val fetchedWarp = WarpRepository.getWarp(request.requestedPositionId, request.type)

        fetchedWarp?.let { warp ->
            val targetWorldRegistryKey = server.worldRegistryKeys.firstOrNull {
                it.value.path == fetchedWarp.worldIdentifier.dimension && it.value.namespace == fetchedWarp.worldIdentifier.env
            }

            val targetWorld = server.getWorld(targetWorldRegistryKey)

            targetWorld?.let { world ->
                val pos = warp.position
                val rot = warp.rotation

                FabAdditions.logger.debug("Loaded world: {}", world.registryKey?.value)

                client.teleport(world, pos.x, pos.y, pos.z, rot.yaw.toFloat(), rot.pitch.toFloat())
            } ?: FabAdditions.logger.error("There is no World for ${warp.worldIdentifier.env} ${warp.worldIdentifier.dimension}")
        } ?: FabAdditions.logger.error("There is no warp for ID: ${request.requestedPositionId} fro type: ${request.type}")
    }

    private fun onClientRequestDelete(client: ServerPlayerEntity, delete: DeleteWarp) {
        WarpRepository.deleteWarp(PlayerRef(client), delete.warp.uniqueId, delete.type)
    }

    private fun onServerReportItemUsage(client: MinecraftClient, openStaff: OpenStaff) {
        client.player?.let { player ->
            screen = FabAdditionsUiScreen(WarpGui(
                openStaff.displayedWarpPositions,
                PlayerRef(player),
                {
                    ClientPlayNetworking.send(
                        Constants.NetworkChannel.Warp.C2S_WARP_REQUEST_PACKET_ID,
                        PacketByteBufs.create().writeString(
                            Gson().toJson(
                                RequestTeleport(
                                    it.uniqueId,
                                    openStaff.type
                                )
                            )
                        )
                    )
                    client.execute { screen?.close() }
                },
                {
                    ClientPlayNetworking.send(
                        Constants.NetworkChannel.Warp.C2S_WARP_ADD_PACKET_ID,
                        PacketByteBufs.create().writeString(
                            Gson().toJson(
                                AddPosition(
                                    it,
                                    openStaff.type
                                )
                            )
                        )
                    )
                    // TODO: Make view update
                    client.execute { screen?.close() }
                },
                {
                    ClientPlayNetworking.send(
                        Constants.NetworkChannel.Warp.C2S_WARP_DELETE_PACKET_ID,
                        PacketByteBufs.create().writeString(
                            Gson().toJson(DeleteWarp(it, openStaff.type))
                        )
                    )
                    // TODO: Make view update
                    client.execute { screen?.close() }
                },
                {
                    client.execute { screen?.close() }
                }
            ))
        }

        client.execute {
            client.setScreen(screen)
        }
    }

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        if (world?.isClient == true || hand != Hand.MAIN_HAND || user?.isSneaking == true) {
            FabAdditions.logger.trace("Is Client. Server must issue action")
            return super.use(world, user, hand)
        }

        // Check that player is server player
        if (user !is ServerPlayerEntity) return super.use(world, user, hand)

        world?.server?.let {
            WarpRepository.initializeCache(it)
        }

        val warps = getWarps(user)

        val openStaff = OpenStaff(
            WarpRepository.getWarpLimit(warpType),
            warpType,
            warps
        )

        val str = Gson().toJson(
            openStaff
        )

        ServerPlayNetworking.send(
            user,
            Constants.NetworkChannel.Warp.S2C_STAFF_ITEM_USAGE_PACKET_ID,
            PacketByteBufs
                .create()
                .writeString(
                    str
                )
        )

        return super.use(world, user, hand)
    }

    abstract fun getWarps(user: ServerPlayerEntity): Set<PlayerWarps>
}