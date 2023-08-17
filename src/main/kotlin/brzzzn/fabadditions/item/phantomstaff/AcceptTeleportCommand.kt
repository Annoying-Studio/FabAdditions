package brzzzn.fabadditions.item.phantomstaff

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.data.PlayerRef
import brzzzn.fabadditions.framework.AbstractCommand
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import kotlinx.coroutines.*
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import java.util.*
import kotlin.collections.HashMap

object AcceptTeleportCommand: AbstractCommand("phantomtpaccept") {

    /**
     * Contains all [Job]s for all currently executing Teleportation coroutines.
     * Mapped by the UUID of the source Player
     */
    private val teleportJobs: HashMap<String, Job> = hashMapOf()

    override fun execute(context: CommandContext<ServerCommandSource>): Int {
        context.source.player?.let { serverPlayerEntity ->
            executeNextTeleportForPlayer(PlayerRef(serverPlayerEntity), context.source.server)
        } ?: FabAdditions.logger.info("Player from command source of command: $baseCommand is null. Command was possibly executed from a console")
        return 1
    }

    private fun executeNextTeleportForPlayer(playerRef: PlayerRef, server: MinecraftServer) {
        val sourcePlayerRef = TeleportQueue.getNextValidRequestTarget(playerRef)
        if (sourcePlayerRef == null) {
            FabAdditions.logger.info("There is currently no player in queue for teleportation to player: ${playerRef.name} : ${playerRef.uuid}")
            return
        }

        val sourcePlayer = server.playerManager.getPlayer(UUID.fromString(sourcePlayerRef.uuid))
        val targetPlayer = server.playerManager.getPlayer(UUID.fromString(playerRef.uuid))

        if (sourcePlayer == null) {
            FabAdditions.logger.error("There was no teleport source player found with UUID: ${sourcePlayerRef.uuid}")
            return
        }

        if (targetPlayer == null) {
            FabAdditions.logger.error("There was no teleport target player found with UUID ${playerRef.uuid}")
            return
        }

        teleportToPlayer(sourcePlayer, targetPlayer)
    }

    /**
     * Server code that runs as soon as player ([user] in this case) tries to teleport to another player [player]
     */
    private fun teleportToPlayer(user: ServerPlayerEntity, player: ServerPlayerEntity) {
        // Add effects
        user.addStatusEffect(
            StatusEffectInstance(StatusEffects.LEVITATION, 3*20, 1, false, false)
        )

        user.addStatusEffect(
            StatusEffectInstance(StatusEffects.NAUSEA, 8*20, 3, false, false)
        )

        user.addStatusEffect(
            StatusEffectInstance(StatusEffects.BLINDNESS, 4*20, 1, false, false)
        )


        // Start coroutine to teleport player after a delay
        if (teleportJobs[user.uuidAsString]?.isActive == true) {
            FabAdditions.logger.warn("There is already a teleport job running for the player ${user.name.string} : ${user.uuidAsString} skipping new teleport")
            return
        }
        teleportJobs[user.uuidAsString] = CoroutineScope(Dispatchers.Default).launch {
            delay(3000)
            user.server.getWorld(player.world.registryKey)
                ?.playSound(null, user.blockPos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.5f, 1f)

            user.teleport(user.server.getWorld(player.world.registryKey), player.x, player.y, player.z, player.headYaw, 0.5f)
        }

        user.setExperienceLevel(user.experienceLevel - PhantomStaff.EXPERIENCE_COST)
    }
}