package brzzzn.fabadditions.registry;

import brzzzn.fabadditions.FabAdditions;
import brzzzn.fabadditions.item.AmethystMirror;
import brzzzn.fabadditions.item.InterdimensionalMirror;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FabItems
{
    public static final Item AMETHYST_MIRROR = registerItem("amethyst_mirror",
            new AmethystMirror((new FabricItemSettings().group(ItemGroup.TOOLS)).maxCount(1)));

    public static final Item INTERDIMENSIONAL_MIRROR = registerItem("interdimensional_mirror",
            new InterdimensionalMirror((new FabricItemSettings().group(ItemGroup.TOOLS)).maxCount(1)));

    private static Item registerItem(String name, Item item)
    {
        return Registry.register(Registry.ITEM, new Identifier(FabAdditions.ID, name), item);
    }

    public static void registerModItems()
    {
        FabAdditions.Companion.getLogger().debug("Registering Mod Items for " + FabAdditions.ID);

    }
}
