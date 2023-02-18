package brzzzn.fabadditions

import brzzzn.fabadditions.registry.FabBlocks
import brzzzn.fabadditions.registry.FabItems
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

class FabAdditions : ModInitializer {
    override fun onInitialize() {
        FabItems.registerModItems()
        FabBlocks.registerModBlocks()
    }

    companion object {
        const val ID = "fabadditions"

		val logger = LoggerFactory.getLogger("fabadditions")
    }
}