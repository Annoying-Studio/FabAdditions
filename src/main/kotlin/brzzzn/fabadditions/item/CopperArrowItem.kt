package brzzzn.fabadditions.item

import brzzzn.fabadditions.entities.arrow.CopperArrowEntity
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ArrowItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

class CopperArrowItem(settings: Settings) : ArrowItem(settings)
{
    override fun createArrow(world: World, stack: ItemStack, shooter: LivingEntity): PersistentProjectileEntity
    {
        return CopperArrowEntity(shooter, world)
    }

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        if (Screen.hasShiftDown())
        {
            tooltip.add(Text.translatable("item.fabadditions.copper_arrow.tooltip").formatted(Formatting.GRAY))
        } else
        {
            tooltip.add(Text.translatable("tooltip.fabadditions.hold_shift").formatted(Formatting.GRAY))
        }
    }
}