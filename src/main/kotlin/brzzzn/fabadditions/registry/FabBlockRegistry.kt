package brzzzn.fabadditions.registry

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.FabAdditions.Companion.logger
import brzzzn.fabadditions.block.Blahaj
import brzzzn.fabadditions.block.ChunkLoader
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

object FabBlockRegistry
{
    val CHUNK_LOADER = registerBlock(
        "chunk_loader",
        ChunkLoader(FabricBlockSettings.create().strength(1.5f).requiresTool()),
        ItemGroups.FUNCTIONAL)

    val BLAHAJ = registerBlock(
        "blahaj",
        Blahaj(FabricBlockSettings.create().strength(0.2f).nonOpaque().sounds(BlockSoundGroup.WOOL)),
            ItemGroups.FUNCTIONAL)

    /**
     * Registers a simple block as a mod block
     */
    private fun registerBlockWithoutItem(name: String, block: Block): Block {
        return Registry.register(Registries.BLOCK, Identifier(FabAdditions.ID, name), block)
    }

    /**
     * Registers a simple block as a mod block with item
     */
    private fun registerBlock(name: String, block: Block, tab: RegistryKey<ItemGroup>): Block {
        registerBlockItem(name, block, tab)
        return Registry.register(Registries.BLOCK, Identifier(FabAdditions.ID, name), block)
    }

    private fun registerBlockItem(name: String, block: Block, tab: RegistryKey<ItemGroup>): Item {
         val item = Registry.register(Registries.ITEM, Identifier(FabAdditions.ID, name),
                BlockItem(block, FabricItemSettings()))
        ItemGroupEvents.modifyEntriesEvent(tab)
            .register(ModifyEntries { content: FabricItemGroupEntries ->
                content.add(
                    item
                )
            })
        return item
    }

    fun registerModBlocks()
    {
        logger.debug("Registering Mod Blocks/Items for " + FabAdditions.ID)
    }
}