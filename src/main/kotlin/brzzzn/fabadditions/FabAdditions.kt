package brzzzn.fabadditions

import brzzzn.fabadditions.registry.*
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

class FabAdditions : ModInitializer {
    override fun onInitialize()
    {
        FabCommandRegistry.registerCommands()
        FabItemsRegistry.registerModItems()
        FabBlockRegistry.registerModBlocks()
        FabBlockEntityRegistry.registerModBlockEntities()
        FabEntityRegistry.registerModEntities()
    }

    companion object {
        const val ID = "fabadditions"

		val logger = LoggerFactory.getLogger("fabadditions")
    }
}