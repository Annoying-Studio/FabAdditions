package brzzzn.fabadditions.client.render.entity

import brzzzn.fabadditions.entities.arrow.BombArrowEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.ProjectileEntityRenderer
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class BombArrowEntityRenderer(context: EntityRendererFactory.Context?) : ProjectileEntityRenderer<BombArrowEntity>(context)
{
    private val texture: Identifier = Identifier("fabadditions:textures/entity/bomb_arrow.png")

    override fun getTexture(entity: BombArrowEntity?): Identifier
    {
        return texture
    }
}