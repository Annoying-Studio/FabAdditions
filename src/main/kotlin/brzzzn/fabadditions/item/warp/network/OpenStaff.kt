package brzzzn.fabadditions.item.warp.network

import brzzzn.fabadditions.data.WarpPosition
import brzzzn.fabadditions.item.warp.PlayerWarps
import brzzzn.fabadditions.item.warp.WarpType

data class OpenStaff (
    /**
     * Max number of warps allowed for the current player
     */
    val maxAllowed: Int,
    val type: WarpType,
    val displayedWarpPositions: Set<PlayerWarps>,
)