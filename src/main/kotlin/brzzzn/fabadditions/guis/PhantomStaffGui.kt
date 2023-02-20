package brzzzn.fabadditions.guis

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WLabel
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text

class PhantomStaffGui(
        val players: Set<PlayerEntity>,
        val teleportLambda: (PlayerEntity) -> Unit
) : LightweightGuiDescription() {
    init {
        val root = WGridPanel()

        setRootPanel(root)

        root.add(WLabel(Text.translatable("item.fabadditions.phantom_staff")), 1, 1)

        MinecraftClient.getInstance().networkHandler?.playerList
        val scrollPanel = WGridPanel()

        for (player in players.withIndex()) {
            val tpButton = WButton(Text.of(player.value.displayName.string ?: ""))
            tpButton.onClick = Runnable { teleportLambda.invoke(player.value) }
            scrollPanel.add(tpButton, 0, player.index)
        }

        root.add(scrollPanel, 0, 0)


        root.setSize(100, 100)
    }
}