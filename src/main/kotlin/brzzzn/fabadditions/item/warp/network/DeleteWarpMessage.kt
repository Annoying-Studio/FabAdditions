package brzzzn.fabadditions.item.warp.network

import brzzzn.fabadditions.data.WarpPosition
import brzzzn.fabadditions.item.warp.WarpType

data class DeleteWarpMessage(
    val warp: WarpPosition,
    val type: WarpType
)
