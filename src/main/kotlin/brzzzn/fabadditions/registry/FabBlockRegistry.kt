package brzzzn.fabadditions.registry

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.FabAdditions.Companion.logger
import brzzzn.fabadditions.block.ChunkLoader
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object FabBlockRegistry
{
    val CHUNK_LOADER = registerBlock(
        "chunk_loader",
        ChunkLoader(FabricBlockSettings.of(Material.METAL).strength(1.5f).requiresTool()),
        ItemGroup.DECORATIONS)

    /**
     * Registers a simple block as a mod block
     */
    private fun registerBlockWithoutItem(name: String, block: Block): Block {
        return Registry.register(Registry.BLOCK, Identifier(FabAdditions.ID, name), block)
    }

    /**
     * Registers a simple block as a mod block with item
     */
    private fun registerBlock(name: String, block: Block, group: ItemGroup): Block {
        registerBlockItem(name, block, group)
        return Registry.register(Registry.BLOCK, Identifier(FabAdditions.ID, name), block)
    }

    private fun registerBlockItem(name: String, block: Block, group: ItemGroup): Item {
        return Registry.register(Registry.ITEM, Identifier(FabAdditions.ID, name),
                BlockItem(block, FabricItemSettings().group(group)))
    }

    fun registerModBlocks()
    {
        logger.debug("Registering Mod Blocks/Items for " + FabAdditions.ID)
    }
}