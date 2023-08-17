package brzzzn.fabadditions.framework

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager.RegistrationEnvironment
import net.minecraft.server.command.ServerCommandSource

abstract class AbstractCommand(
    var baseCommand: String,
    vararg var additionalCommands: String
) {
    abstract fun execute(context: CommandContext<ServerCommandSource>): Int
}