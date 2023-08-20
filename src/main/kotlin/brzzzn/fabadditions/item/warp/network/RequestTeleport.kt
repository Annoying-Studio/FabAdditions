package brzzzn.fabadditions.item.warp.network

import brzzzn.fabadditions.item.warp.WarpType

data class RequestTeleport(
    val requestedPositionId: String,
    val type: WarpType
)
