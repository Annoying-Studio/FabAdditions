package brzzzn.fabadditions.item.warp

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

class PylonStaff(settings: Settings) : Item(settings) {
    override fun appendTooltip(stack: ItemStack?, world: World?, tooltip: MutableList<Text>?, context: TooltipContext?) {
        if (Screen.hasShiftDown()) {
            tooltip?.add(Text.translatable("item.fabadditions.warp_staff.tooltip").formatted(Formatting.GRAY))
        } else {
            tooltip?.add(Text.translatable("tooltip.fabadditions.hold_shift").formatted(Formatting.GRAY))
        }
        super.appendTooltip(stack, world, tooltip, context)
    }
}