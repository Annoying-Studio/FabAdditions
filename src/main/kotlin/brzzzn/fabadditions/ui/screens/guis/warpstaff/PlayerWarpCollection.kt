package brzzzn.fabadditions.ui.screens.guis.warpstaff

import brzzzn.fabadditions.data.PlayerRef
import brzzzn.fabadditions.data.WarpPosition
import brzzzn.fabadditions.item.warp.PlayerWarps
import io.github.cottonmc.cotton.gui.widget.*
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class PlayerWarpCollection : WPlainPanel() {

    fun create(
        player: PlayerRef,
        warpSet: PlayerWarps,
        onSelectWarp: (WarpPosition) -> Unit,
        onDeleteWarp: (WarpPosition) -> Unit,
        finalWidth: Int
    ) {

        var currentHeight = 0
        val playerNameLabel = WLabel(
            Text.of(warpSet.player.name).apply {
                style
                    .withFormatting(Formatting.BOLD)
                    .withColor(Formatting.GOLD)
            }
        )

        this.add(
            playerNameLabel,
            0,
            0,
            finalWidth*18,
            1*18
        )

        currentHeight += 18

        for ((index, warp) in warpSet.warps.sortedBy { it.name }.withIndex()) {
            val teleportButton = WButton(Text.of(warp.name))
            val deleteButton = WButton(Text.translatable("item.fabadditions.warp.view.delete"))

            teleportButton.onClick = Runnable {
                onSelectWarp.invoke(warp)
            }

            deleteButton.isEnabled = warpSet.player == player
            deleteButton.onClick = Runnable {
                onDeleteWarp.invoke(warp)
            }

            this.add(
                teleportButton,
                0, index*18 + 12, (finalWidth - 5)*18, 1*18
            )

            this.add(
                deleteButton,
                (finalWidth - 5)*18, index*18 +12, 4*18, 1*18
            )
            currentHeight += 18
        }

        this.setSize(finalWidth * 18, currentHeight)
    }
}