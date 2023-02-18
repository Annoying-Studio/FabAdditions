package brzzzn.fabadditions;

import brzzzn.fabadditions.registry.FabBlocks;
import brzzzn.fabadditions.registry.FabItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabAdditions implements ModInitializer {

	public static final String ID = "fabadditions";

	public static final Logger LOGGER = LoggerFactory.getLogger("fabadditions");

	@Override
	public void onInitialize()
	{
		FabItems.registerModItems();
		FabBlocks.registerModBlocks();
	}
}