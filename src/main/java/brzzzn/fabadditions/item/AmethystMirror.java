package brzzzn.fabadditions.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AmethystMirror extends Item
{
    boolean mirrors_enabled = true;

    public AmethystMirror(Settings settings)
    {
        super(settings);

    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        if(!world.isClient && hand == Hand.MAIN_HAND) //Server Only
        {
            if(mirrors_enabled)
            {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) user;
                BlockPos pos = serverPlayer.getSpawnPointPosition();
                serverPlayer.teleport(pos.getX(), pos.getY(), pos.getZ());
            }
            else
            {
                user.sendMessage(Text.literal("Mirrors are currently disabled by config."));
            }

            user.getItemCooldownManager().set(this, 40);
        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
    {
        if(Screen.hasShiftDown())
        {
            tooltip.add(Text.translatable("item.fabadditions.amethyst_mirror.tooltip").formatted(Formatting.GRAY));
        }
        else{
            tooltip.add(Text.translatable("global.fabadditions.tooltip").formatted(Formatting.GRAY));
        }
    }
}
