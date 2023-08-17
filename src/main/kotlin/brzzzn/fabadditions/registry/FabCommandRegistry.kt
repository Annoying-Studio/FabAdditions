package brzzzn.fabadditions.registry

import brzzzn.fabadditions.framework.AbstractCommand
import brzzzn.fabadditions.item.phantomstaff.AcceptTeleportCommand
import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.CommandNode
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.ArgumentTypes
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

object FabCommandRegistry {
    fun registerCommands() {
        registerCommand(AcceptTeleportCommand)
    }

    private fun registerCommand(command: AbstractCommand) {
        CommandRegistrationCallback.EVENT.register(
            CommandRegistrationCallback { dispatcher, registryAccess, environment ->
                dispatcher.register(
                    buildCommandLiteral(command)
                )
            }
        )
    }

    private fun buildCommandLiteral(command: AbstractCommand) : LiteralArgumentBuilder<ServerCommandSource> {
        val resultingCommand = CommandManager.literal(command.baseCommand)

        for (additionalArgument in command.additionalCommands) {
            resultingCommand.then(
                CommandManager.literal(additionalArgument)
            )
        }

        resultingCommand.executes { context ->
            command.execute(context)
        }

        return resultingCommand
    }
}