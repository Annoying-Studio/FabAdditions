package brzzzn.fabadditions.events

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer

object ServerTickEventHandler: ServerTickEvents.EndTick {
    override fun onEndTick(server: MinecraftServer?) {
        TODO("Not yet implemented")
    }
}