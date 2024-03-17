package brzzzn.fabadditions.ui.screens.guis.phantomstaff

import brzzzn.fabadditions.data.PlayerList
import brzzzn.fabadditions.data.PlayerRef
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WListPanel
import net.minecraft.text.Text

class PhantomStaffGui(
    players: PlayerList,
    private val teleportLambda: (PlayerRef) -> Unit
) : LightweightGuiDescription() {
    init {
        val root = WGridPanel()

        setRootPanel(root)

        root.add(
            WLabel(
                Text.translatable("item.fabadditions.phantom_staff")),
            1,
            1,
            9,
            1
        )

        val list = WListPanel(
            players.players,
            { WButton() },
            { player, button ->
                button.label = Text.of(player.name)
                button.setSize(root.width, 18)
                button.onClick = Runnable {
                    teleportLambda.invoke(player)
                }
            }
        )

        root.add(list, 1, 2, 10, 9)

        root.setSize(12*18, 12*18)
    }
}