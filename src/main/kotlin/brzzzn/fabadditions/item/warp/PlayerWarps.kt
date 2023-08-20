package brzzzn.fabadditions.item.warp

import brzzzn.fabadditions.data.PlayerRef
import brzzzn.fabadditions.data.WarpPosition

data class PlayerWarps(
    val player: PlayerRef,
    val warps: HashSet<WarpPosition>
)
