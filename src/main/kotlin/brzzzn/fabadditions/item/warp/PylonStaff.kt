package brzzzn.fabadditions.item.warp

import brzzzn.fabadditions.data.PlayerRef
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

class PylonStaff(
    settings: Settings
) : AbstractWarpStaffItem(
    settings,
    WarpType.PYLON
) {
    override fun getWarps(user: ServerPlayerEntity): Set<PlayerWarps> {
        return setOf(
            WarpRepository.getPlayerSpecificWarps(
                PlayerRef(
                    user.name.string,
                    user.uuidAsString,
                )
            )
        )
    }

    override fun appendTooltip(stack: ItemStack?, world: World?, tooltip: MutableList<Text>?, context: TooltipContext?) {
        if (Screen.hasShiftDown()) {
            tooltip?.add(Text.translatable("item.fabadditions.warp_staff.tooltip").formatted(Formatting.GRAY))
        } else {
            tooltip?.add(Text.translatable("tooltip.fabadditions.hold_shift").formatted(Formatting.GRAY))
        }
        super.appendTooltip(stack, world, tooltip, context)
    }
}