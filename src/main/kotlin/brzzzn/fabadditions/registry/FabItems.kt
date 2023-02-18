package brzzzn.fabadditions.registry

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.FabAdditions.Companion.logger
import brzzzn.fabadditions.item.AmethystFeather
import brzzzn.fabadditions.item.InterdimensionalFeather
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object FabItems {
    val AMETHYST_FEATHER = registerItem("amethyst_feather",
            AmethystFeather(FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)))
    val INTERDIMENSIONAL_FEATHER = registerItem("interdimensional_feather",
            InterdimensionalFeather(FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)))

    private fun registerItem(name: String, item: Item): Item {
        return Registry.register(Registry.ITEM, Identifier(FabAdditions.ID, name), item)
    }

    fun registerModItems() {
        logger.debug("Registering Mod Items for " + FabAdditions.ID)
    }
}