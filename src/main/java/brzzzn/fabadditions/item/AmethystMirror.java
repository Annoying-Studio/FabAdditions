package brzzzn.fabadditions.item;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EntityType;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class AmethystMirror extends Item
{
    boolean mirrors_enabled = true; //placeholder
    boolean isInterdimensional = false;

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
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) user;
                ServerWorld targetWorld = serverPlayer.server.getWorld(serverPlayer.getSpawnPointDimension());

                BlockPos spawnPos = serverPlayer.getSpawnPointPosition();

                if (spawnPos != null)
                {
                    Block respawnBlock = targetWorld.getBlockState(spawnPos).getBlock();
                    Optional<Vec3d> respawnPos = Optional.empty();

                    if (respawnBlock instanceof RespawnAnchorBlock)
                    {
                        respawnPos = RespawnAnchorBlock.findRespawnPosition(EntityType.PLAYER, targetWorld, spawnPos);

                    } else if (respawnBlock instanceof BedBlock) {
                        respawnPos = BedBlock.findWakeUpPosition(EntityType.PLAYER, targetWorld, spawnPos, serverPlayer.getSpawnAngle());
                    }

                    if(respawnPos.isPresent())
                    {
                        //teleport
                        if (!isInterdimensional && serverPlayer.getWorld() != targetWorld)
                        {
                            user.sendMessage(Text.translatable("global.fabadditions.wrong_dimension").formatted(Formatting.RED));
                        }
                        else
                        {
                            Vec3d spawnVec = respawnPos.get();
                            serverPlayer.teleport(targetWorld, spawnVec.getX(), spawnVec.getY(), spawnVec.getZ(), serverPlayer.getSpawnAngle(), 0.5F);

                            //effects
                            serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 30));
                            targetWorld.playSound(null, spawnPos, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 0.5f, 1f);
                        }
                    }
                    else
                    {
                        user.sendMessage(Text.translatable("global.fabadditions.no_respawn").formatted(Formatting.RED));
                    }
                }
                else
                {
                    user.sendMessage(Text.translatable("global.fabadditions.no_respawn").formatted(Formatting.RED));
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