package brzzzn.fabadditions.item.warp.network

import brzzzn.fabadditions.item.warp.WarpType

data class AddPositionMessage(
    val positionName: String,
    val type: WarpType,
)
