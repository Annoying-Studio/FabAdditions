package brzzzn.fabadditions.ui.guis.warpstaff

import brzzzn.fabadditions.data.PlayerRef
import brzzzn.fabadditions.data.WarpPosition
import brzzzn.fabadditions.item.warp.PlayerWarps
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WScrollPanel
import io.github.cottonmc.cotton.gui.widget.WTextField
import net.fabricmc.fabric.api.util.TriState
import net.minecraft.text.Text

class WarpGui(
    /**
     * Set of all warps that the server allowed to be displayed
     */
    private val warps: Set<PlayerWarps>,
    /**
     * Current Player
     */
    private val self: PlayerRef,

    /**
     * Lambda executed when a warp is selected
     */
    private val onSelectWarp: (WarpPosition) -> Unit,
    /**
     * Lambda executed when a player adds a warp to the current list.
     */
    private val onAddWarp: (String) -> Unit,

    /**
     * Lambda executed when a player deletes a warp from the current list.
     */
    private val onDeleteWarp: (WarpPosition) -> Unit,

    private val onClose: () -> Unit
) : LightweightGuiDescription() {
    init {
        val root = WGridPanel()

        setRootPanel(root)

        root.add(
            WButton(
                Text.translatable("button.fabadditions.exit")
            ).apply {
                onClick = Runnable(onClose)
            },
            16,
            0,
            1,
            1
        )

        val topLabel = WLabel(
            Text.translatable("item.fabadditions.warp.view.name")
        )

        val addPositionTextField = WTextField(
            Text.translatable("item.fabadditions.warp.view.new.template")
        )

        val addPositionButton = WButton(
            Text.translatable("item.fabadditions.warp.view.new.button")
        ).apply {
            onClick = Runnable {
                onAddWarp.invoke(
                    addPositionTextField.text
                )
            }
        }

        val playerWarpList = WScrollPanel(
            PlayerWarpList(
                warps,
                self,
                onSelectWarp,
                onDeleteWarp,
                15
            )
        )

        playerWarpList.setScrollingHorizontally(TriState.FALSE)
        playerWarpList.setScrollingVertically(TriState.DEFAULT)

        root.add(
            topLabel,
            1,
            1,
            15,
            1
        )

        root.add(
            playerWarpList,
            1,
            2,
            15,
            6
        )

        root.add(
            addPositionTextField,
            1,
            9,
            15,
            1
        )

        root.add(
            addPositionButton,
            1,
            10,
            15,
            1
        )

        root.setSize(17*18, 12*18)
    }
}