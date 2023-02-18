package brzzzn.fabadditions.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InterdimensionalMirror extends AmethystMirror
{
    public InterdimensionalMirror(Settings settings)
    {
        super(settings);
        isInterdimensional = true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
    {
        if(Screen.hasShiftDown())
        {
            tooltip.add(Text.translatable("item.fabadditions.interdimensional_mirror.tooltip").formatted(Formatting.GRAY));
        }
        else
        {
            tooltip.add(Text.translatable("global.fabadditions.tooltip").formatted(Formatting.GRAY));
        }
    }
}