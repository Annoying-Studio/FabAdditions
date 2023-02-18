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
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*

open class AmethystFeather(settings: Settings?) : Item(settings) {
    var mirrors_enabled = true //placeholder
    var isInterdimensional = false
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient && hand == Hand.MAIN_HAND && !user.isSneaking) {
            if (mirrors_enabled) {
                val serverPlayer = user as ServerPlayerEntity
                val targetWorld = serverPlayer.server.getWorld(serverPlayer.spawnPointDimension)
                val spawnPos = serverPlayer.spawnPointPosition
                if (spawnPos != null) {
                    val respawnBlock = targetWorld!!.getBlockState(spawnPos).block
                    var respawnPos: Optional<Vec3d> = Optional.empty<Vec3d>()
                    if (respawnBlock is RespawnAnchorBlock) {
                        respawnPos = RespawnAnchorBlock.findRespawnPosition(EntityType.PLAYER, targetWorld, spawnPos)
                    } else if (respawnBlock is BedBlock) {
                        respawnPos = BedBlock.findWakeUpPosition(EntityType.PLAYER, targetWorld, spawnPos, serverPlayer.spawnAngle)
                    }
                    if (respawnPos.isPresent) {
                        //teleport
                        if (!isInterdimensional && serverPlayer.getWorld() !== targetWorld) {
                            user.sendMessage(Text.translatable("global.fabadditions.wrong_dimension").formatted(Formatting.RED))
                        } else {
                            val spawnVec = respawnPos.get()
                            serverPlayer.teleport(targetWorld, spawnVec.getX(), spawnVec.getY(), spawnVec.getZ(), serverPlayer.spawnAngle, 0.5f)

                            //effects
                            serverPlayer.addStatusEffect(StatusEffectInstance(StatusEffects.BLINDNESS, 30))
                            targetWorld.playSound(null, spawnPos, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 0.5f, 1f)
                        }
                    } else {
                        user.sendMessage(Text.translatable("global.fabadditions.no_respawn").formatted(Formatting.RED))
                    }
                } else {
                    user.sendMessage(Text.translatable("global.fabadditions.no_respawn").formatted(Formatting.RED))
                }
            } else {
                user.sendMessage(Text.translatable("global.fabadditions.config_disabled").formatted(Formatting.GOLD))
            }
            user.itemCooldownManager[this] = 40
        }
        return super.use(world, user, hand)
    }

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("item.fabadditions.amethyst_feather.tooltip").formatted(Formatting.GRAY))
        } else {
            tooltip.add(Text.translatable("global.fabadditions.tooltip").formatted(Formatting.GRAY))
        }
    }
}