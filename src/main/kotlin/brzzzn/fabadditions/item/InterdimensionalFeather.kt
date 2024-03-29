package brzzzn.fabadditions.item

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

class InterdimensionalFeather(settings: Settings?) : AmethystFeather(settings) {
    init {
        isInterdimensional = true
        experienceCost = 0
    }

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("item.fabadditions.interdimensional_feather.tooltip").formatted(Formatting.GRAY))
        } else {
            tooltip.add(Text.translatable("tooltip.fabadditions.hold_shift").formatted(Formatting.GRAY))
        }
    }
}