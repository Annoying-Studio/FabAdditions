package brzzzn.fabadditions.registry

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.FabAdditions.Companion.logger
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object FabBlocks {
    private fun registerBlockWithoutItem(name: String, block: Block): Block {
        return Registry.register(Registry.BLOCK, Identifier(FabAdditions.ID, name), block)
    }

    private fun registerBlock(name: String, block: Block, group: ItemGroup): Block {
        registerBlockItem(name, block, group)
        return Registry.register(Registry.BLOCK, Identifier(FabAdditions.ID, name), block)
    }

    private fun registerBlockItem(name: String, block: Block, group: ItemGroup): Item {
        return Registry.register(Registry.ITEM, Identifier(FabAdditions.ID, name),
                BlockItem(block, FabricItemSettings().group(group)))
    }

    fun registerModBlocks() {
        logger.debug("Registering Mod Blocks/Items for " + FabAdditions.ID)
    }
}