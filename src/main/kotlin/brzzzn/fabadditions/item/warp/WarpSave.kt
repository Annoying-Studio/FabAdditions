package brzzzn.fabadditions.item.warp

data class WarpSave (
    val playerSpecific: HashSet<PlayerWarps>,
    val worldWarps: HashSet<PlayerWarps>
)