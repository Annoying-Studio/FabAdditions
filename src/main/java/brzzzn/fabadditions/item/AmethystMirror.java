package brzzzn.fabadditions.item;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AmethystMirror extends Item
{
    boolean mirrors_enabled = true; //placeholder

    public AmethystMirror(Settings settings)
    {
        super(settings);

    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        if(!world.isClient && hand == Hand.MAIN_HAND && !user.isSneaking())
        {
            if(mirrors_enabled)
            {
                ServerWorld serverWorld = ((ServerWorld) world).getServer().getWorld(World.OVERWORLD);
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) user;

                if(serverPlayer.getSpawnPointPosition()==null)
                {
                    user.sendMessage(Text.translatable("global.fabadditions.no_respawn").formatted(Formatting.RED));
                }
                else
                {
                    BlockPos bedPos = serverPlayer.getSpawnPointPosition();

                    serverPlayer.stopRiding();

                    TeleportTarget target = new TeleportTarget(new Vec3d(bedPos.getX() + 0.5F, bedPos.getY(), bedPos.getZ() + 0.5F),
                            new Vec3d(0, 0, 0), serverPlayer.getYaw(), serverPlayer.getPitch());
                    teleport(serverPlayer, serverWorld, target);

                    world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(),
                            SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 0.5F, 1F);
                    serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 30));
                }
            }
            else
            {
                user.sendMessage(Text.translatable("global.fabadditions.config_disabled").formatted(Formatting.GOLD));
            }

            user.getItemCooldownManager().set(this, 40);
        }

        return super.use(world, user, hand);
    }

    private void teleport(ServerPlayerEntity serverPlayer, ServerWorld servWorld, TeleportTarget tpTarget)
    {
        if(serverPlayer.world.getRegistryKey().equals(servWorld.getRegistryKey()))
        {
            serverPlayer.networkHandler.requestTeleport(tpTarget.position.getX(), tpTarget.position.getY(), tpTarget.position.getZ(), tpTarget.yaw, tpTarget.pitch);
        }
        else
        {
            FabricDimensions.teleport(serverPlayer, servWorld, tpTarget);
        }
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
