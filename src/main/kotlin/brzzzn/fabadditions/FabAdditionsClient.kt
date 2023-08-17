package brzzzn.fabadditions

import brzzzn.fabadditions.client.render.entity.BombArrowEntityRenderer
import brzzzn.fabadditions.client.render.entity.CopperArrowEntityRenderer
import brzzzn.fabadditions.registry.FabBlockRegistry
import brzzzn.fabadditions.registry.FabEntityRegistry
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.RenderLayer

class FabAdditionsClient : ClientModInitializer {
    override fun onInitializeClient()
    {
        initEntityRenderer()
    }

    private fun initEntityRenderer()
    {
        EntityRendererRegistry.register(FabEntityRegistry.COPPER_ARROW, ::CopperArrowEntityRenderer)
        EntityRendererRegistry.register(FabEntityRegistry.BOMB_ARROW, ::BombArrowEntityRenderer)
    }
}