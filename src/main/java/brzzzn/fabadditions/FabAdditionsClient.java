package brzzzn.fabadditions;

import brzzzn.fabadditions.registry.FabBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class FabAdditionsClient implements ClientModInitializer
{

    @Override
    public void onInitializeClient()
    {
        //Glass Layers

        /*
        .INSTANCE.putBlock(FabBlocks.ENCASED_GLOWSTONE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(FabBlocks.ENCASED_SEA_LANTERN, RenderLayer.getTranslucent());
        */
    }
}
