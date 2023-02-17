package brzzzn.fabadditions.registry;

import brzzzn.fabadditions.FabAdditions;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FabBlocks
{
    /*
    public static final Block ENCASED_GLOWSTONE = registerBlock("encased_glowstone",
            new GlassBlock(FabricBlockSettings.of(Material.GLASS).strength(0.3f, 0.3f)
                    .sounds(BlockSoundGroup.GLASS).luminance(15).nonOpaque()), ItemGroup.DECORATIONS);

    public static final Block ENCASED_SEA_LANTERN = registerBlock("encased_sea_lantern",
            new GlassBlock(FabricBlockSettings.of(Material.GLASS).strength(0.3f, 0.3f)
                    .sounds(BlockSoundGroup.GLASS).luminance(15).nonOpaque()), ItemGroup.DECORATIONS);
     */

    private static Block registerBlockWithoutItem(String name, Block block)
    {
        return Registry.register(Registry.BLOCK, new Identifier(FabAdditions.ID, name), block);
    }

    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(FabAdditions.ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(FabAdditions.ID, name),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

    public static void registerModBlocks()
    {
        FabAdditions.LOGGER.debug("Registering Mod Blocks/Items for " + FabAdditions.ID);
    }
}