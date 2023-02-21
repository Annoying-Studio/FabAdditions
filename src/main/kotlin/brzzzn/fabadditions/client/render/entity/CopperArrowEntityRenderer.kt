package brzzzn.fabadditions.client.render.entity

import brzzzn.fabadditions.entities.arrow.CopperArrowEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.ProjectileEntityRenderer
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class CopperArrowEntityRenderer(context: EntityRendererFactory.Context?) : ProjectileEntityRenderer<CopperArrowEntity>(context)
{
    private val texture: Identifier = Identifier("fabadditions:textures/entity/copper_arrow.png")

    override fun getTexture(entity: CopperArrowEntity?): Identifier
    {
        return texture
    }
}