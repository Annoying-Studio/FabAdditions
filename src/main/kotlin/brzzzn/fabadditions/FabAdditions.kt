package brzzzn.fabadditions

import brzzzn.fabadditions.registry.FabBlocks
import brzzzn.fabadditions.registry.FabEntityRegistry
import brzzzn.fabadditions.registry.FabItemsRegistry
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

class FabAdditions : ModInitializer {
    override fun onInitialize() {
        FabItemsRegistry.registerModItems()
        FabBlocks.registerModBlocks()
        FabEntityRegistry.registerModEntities()
    }

    companion object {
        const val ID = "fabadditions"

		val logger = LoggerFactory.getLogger("fabadditions")
    }
}