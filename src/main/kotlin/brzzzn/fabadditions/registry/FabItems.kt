package brzzzn.fabadditions.registry

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.FabAdditions.Companion.logger
import brzzzn.fabadditions.item.AmethystMirror
import brzzzn.fabadditions.item.InterdimensionalMirror
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object FabItems {
    val AMETHYST_MIRROR = registerItem("amethyst_mirror",
            AmethystMirror(FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)))
    val INTERDIMENSIONAL_MIRROR = registerItem("interdimensional_mirror",
            InterdimensionalMirror(FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)))

    private fun registerItem(name: String, item: Item): Item {
        return Registry.register(Registry.ITEM, Identifier(FabAdditions.ID, name), item)
    }

    fun registerModItems() {
        logger.debug("Registering Mod Items for " + FabAdditions.ID)
    }
}