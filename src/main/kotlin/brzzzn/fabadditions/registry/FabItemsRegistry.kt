package brzzzn.fabadditions.registry

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.FabAdditions.Companion.logger
import brzzzn.fabadditions.item.AmethystFeather
import brzzzn.fabadditions.item.InterdimensionalFeather
import brzzzn.fabadditions.item.PhantomStaff
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry

object FabItemsRegistry {

    //region default settings
    private val rareToolSettings: FabricItemSettings = FabricItemSettings()
            .group(ItemGroup.TOOLS)
            .maxCount(1)
            .rarity(Rarity.UNCOMMON)
    //endregion

    /**
     * Registers a simple item as a mod item
     */
    private fun registerItem(name: String, item: Item): Item {
        return Registry.register(Registry.ITEM, Identifier(FabAdditions.ID, name), item)
    }

    /**
     * Registers all modded items
     */
    fun registerModItems() {
        logger.debug("Registering Mod Items for ${FabAdditions.ID}")
        // Call all registers
        registerTools()

        logger.debug("Done registering Mod Items for ${FabAdditions.ID}")
    }

    private fun registerTools() {
        // Amethyst Feather
        registerItem(
                "amethyst_feather",
                AmethystFeather(rareToolSettings)
        )
        // Interdimensional Feather
        registerItem(
                "interdimensional_feather",
                InterdimensionalFeather(rareToolSettings)
        )
        // Phantom staff
        registerItem(
                "phantom_staff",
                PhantomStaff(rareToolSettings)
        )
    }
}