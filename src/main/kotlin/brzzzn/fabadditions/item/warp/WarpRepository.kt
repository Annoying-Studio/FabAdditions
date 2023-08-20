package brzzzn.fabadditions.item.warp

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.data.PlayerRef
import brzzzn.fabadditions.data.WarpPosition
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.minecraft.server.MinecraftServer
import net.minecraft.util.WorldSavePath
import java.io.BufferedWriter
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path

object WarpRepository {
    private var cache: WarpSave? = null
    const val saveFile = "Warps.json"
    private var currentPath: Path? = null

    val maxAllowedWarps: Map<WarpType, Int> = mapOf(
        WarpType.WARP to 2,
        WarpType.PYLON to 3
    )

    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    fun createWarp(player: PlayerRef, position: WarpPosition, type: WarpType) {
        cache?.let { save ->
            val warpCollection = when (type) {
                WarpType.WARP -> save.worldWarps
                WarpType.PYLON -> save.playerSpecific
            }

            var existingWarpCollection = warpCollection.firstOrNull {
                it.player == player
            }

            if (existingWarpCollection == null) {
                existingWarpCollection = PlayerWarps(
                    player,
                    hashSetOf()
                )

                warpCollection.add(
                    existingWarpCollection
                )
            }

            existingWarpCollection.warps.add(
                position
            )
        }

        saveCacheToDisk()
    }

    fun getAllGlobalWarps() : Set<PlayerWarps> {
        return cache?.worldWarps ?: emptySet()
    }

    fun getWarpsFromPlayer(player: PlayerRef, type: WarpType) : PlayerWarps {
        return when (type) {
            WarpType.WARP -> cache?.worldWarps?.firstOrNull {
                it.player == player
            }
            WarpType.PYLON -> cache?.playerSpecific?.firstOrNull {
                it.player == player
            }
        } ?: PlayerWarps(
            player,
            hashSetOf()
        )
    }

    fun getPlayerSpecificWarps(player: PlayerRef): PlayerWarps {
        return cache?.playerSpecific?.firstOrNull {
            it.player == player
        } ?: PlayerWarps(player, hashSetOf())
    }

    fun getWarp(uniqueId: String, type: WarpType): WarpPosition? {
        cache?.let { save ->
            val warpCollection = when (type) {
                WarpType.WARP -> save.worldWarps
                WarpType.PYLON -> save.playerSpecific
            }

            warpCollection.forEach { playerWarps ->
                playerWarps.warps.forEach {
                    if (it.uniqueId == uniqueId) {
                        return it
                    }
                }
            }
        }

        return null
    }

    fun deleteWarp(player: PlayerRef, identifier: String, type: WarpType) {
        cache?.let { save ->
            val warpCollection = when (type) {
                WarpType.WARP -> save.worldWarps
                WarpType.PYLON -> save.playerSpecific
            }

            warpCollection.forEach {
                if (it.player == player) {
                    it.warps.removeIf { warp ->
                        warp.uniqueId == identifier
                    }
                } else {
                    FabAdditions.logger.error("There is no Warp with ID: $identifier for player: $player")
                }
            }
        }
    }

    fun initializeCache(server: MinecraftServer) {
        if (cache != null) {
            return
        }

        currentPath = getSaveFilePath(server)

        currentPath?.let { path ->
            if (Files.exists(path)) {
                try {
                    val content = Files.readString(path)

                    cache = Gson().fromJson(content, WarpSave::class.java)


                } catch (e: Exception) {
                    generateEmptyCache()
                }
            }
            else {
                generateEmptyCache()
            }
        }

        saveCacheToDisk()
    }

    private fun generateEmptyCache() {
        cache = WarpSave(
            hashSetOf(),
            hashSetOf()
        )
    }

    private fun saveCacheToDisk() {
        currentPath?.let { path ->
            val json = gson.toJson(cache)

            BufferedWriter(
                FileWriter(path.toString())
            ).use {
                it.write(json)
            }
        }
    }

    private fun getSaveFilePath(server: MinecraftServer): Path {
        return server.getSavePath(WorldSavePath.ROOT).parent.resolve(saveFile)
    }

}