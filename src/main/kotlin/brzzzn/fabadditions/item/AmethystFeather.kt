package brzzzn.fabadditions.item

import net.minecraft.block.BedBlock
import net.minecraft.block.RespawnAnchorBlock
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*

open class AmethystFeather(settings: Settings?) : Item(settings) {
    protected var isInterdimensional : Boolean = false
    protected var experienceCost : Int = 0

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient && hand == Hand.MAIN_HAND && !user.isSneaking) {
            val serverPlayer = user as ServerPlayerEntity
            val targetWorld = serverPlayer.server.getWorld(serverPlayer.spawnPointDimension)
            val spawnPos = serverPlayer.spawnPointPosition

            if(spawnPos == null)
            {
                user.sendMessage(Text.translatable("chat.fabadditions.no_respawn").formatted(Formatting.RED))
                return super.use(world, user, hand)
            }

            val respawnBlock = targetWorld!!.getBlockState(spawnPos).block
            var respawnPos: Optional<Vec3d> = Optional.empty<Vec3d>()

            // Check for type of respawn location
            if (respawnBlock is RespawnAnchorBlock)
            {
                respawnPos = RespawnAnchorBlock.findRespawnPosition(EntityType.PLAYER, targetWorld, spawnPos)
            }
            else if (respawnBlock is BedBlock)
            {
                //I have no clue what Direction we should give here??
                respawnPos = BedBlock.findWakeUpPosition(EntityType.PLAYER, targetWorld, spawnPos, Direction.NORTH, serverPlayer.spawnAngle)
            }

            if (respawnPos.isPresent)
            {
                if (!isInterdimensional && serverPlayer.world !== targetWorld)
                {
                    user.sendMessage(Text.translatable("chat.fabadditions.wrong_dimension").formatted(Formatting.RED))
                }
                else
                {
                    // stop if the player does not have enough xp
                    if(user.experienceLevel < experienceCost)
                    {
                        user.sendMessage(Text.translatable("chat.fabadditions.not_enough_xp").formatted(Formatting.RED))
                        return super.use(world, user, hand)
                    }

                    // Actual teleport logic. Should probably move to its own func later.

                    val spawnVec = respawnPos.get()
                    serverPlayer.teleport(targetWorld, spawnVec.getX(), spawnVec.getY(), spawnVec.getZ(), serverPlayer.spawnAngle, 0.5f)

                    //effects
                    serverPlayer.addStatusEffect(StatusEffectInstance(StatusEffects.BLINDNESS, 30))
                    targetWorld.playSound(null, spawnPos, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 0.5f, 1f)
                }
            }
            else
            {
                user.sendMessage(Text.translatable("chat.fabadditions.no_respawn").formatted(Formatting.RED))
            }

            user.itemCooldownManager[this] = 40
            if(experienceCost > 0)
                user.setExperienceLevel(user.experienceLevel - experienceCost)
        }
        return super.use(world, user, hand)
    }

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("item.fabadditions.amethyst_feather.tooltip").formatted(Formatting.GRAY))
        } else {
            tooltip.add(Text.translatable("tooltip.fabadditions.hold_shift").formatted(Formatting.GRAY))
        }
    }
}