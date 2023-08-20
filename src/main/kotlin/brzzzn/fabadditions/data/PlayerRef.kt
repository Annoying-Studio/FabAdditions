package brzzzn.fabadditions.data

import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity

data class PlayerRef(
    val name: String,
    val uuid: String
) {
    constructor(player: PlayerEntity) : this(player.name.string, player.uuidAsString)
}