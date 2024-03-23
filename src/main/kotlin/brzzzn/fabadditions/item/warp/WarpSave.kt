package brzzzn.fabadditions.item.warp

data class WarpSave (
    val maxWarps: Map<WarpType, Int> = mapOf(
        WarpType.WARP to 2,
        WarpType.PYLON to 3
    ),
    val playerSpecific: HashSet<PlayerWarps>,
    val worldWarps: HashSet<PlayerWarps>
)