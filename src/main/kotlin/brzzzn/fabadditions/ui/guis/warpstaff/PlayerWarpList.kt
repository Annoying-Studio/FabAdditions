package brzzzn.fabadditions.ui.guis.warpstaff

import brzzzn.fabadditions.data.PlayerRef
import brzzzn.fabadditions.data.WarpPosition
import brzzzn.fabadditions.item.warp.PlayerWarps
import io.github.cottonmc.cotton.gui.widget.WPlainPanel

class PlayerWarpList(
    /**
     * Set of all warps that the server allowed to be displayed
     */
    warps: Set<PlayerWarps>,
    /**
     * Current Player
     */
    self: PlayerRef,

    /**
     * Lambda executed when a warp is selected
     */
    onSelectWarp: (WarpPosition) -> Unit,
    /**
     * Lambda executed when a player deletes a warp from the current list.
     */
    deleteWarp: (WarpPosition) -> Unit,
    finalWidth: Int
): WPlainPanel() {

    init {
        var currentHeight = 0
        warps
            .sortedBy { warpEntry -> warpEntry.player.name }
            .sortedBy { warpEntry -> warpEntry.player.name != self.name }
            .forEach { warp ->
            val collection = PlayerWarpEntry()
            collection.create(
                self,
                warp,
                onSelectWarp,
                deleteWarp,
                finalWidth
            )
            this.add(
                collection,
                0,
                currentHeight,
            )

            currentHeight += warp.warps.size * 18 + 21
        }

        this.setSize(finalWidth*18, currentHeight)
    }
}