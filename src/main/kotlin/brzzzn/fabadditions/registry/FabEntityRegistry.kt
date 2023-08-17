package brzzzn.fabadditions.registry

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.entities.arrow.BombArrowEntity
import brzzzn.fabadditions.entities.arrow.CopperArrowEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.registry.Registry

object FabEntityRegistry
{
    val COPPER_ARROW = registerEntity(
        "copper_arrow",
        FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::CopperArrowEntity)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .trackRangeBlocks(4).trackedUpdateRate(20).build()
    )

    val BOMB_ARROW = registerEntity(
        "bomb_arrow",
        FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::BombArrowEntity)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .trackRangeBlocks(4).trackedUpdateRate(20).build()
    )

    private fun <T : Entity?> registerEntity(name: String, entityType: EntityType<T>): EntityType<T>?
    {
        return Registry.register(Registries.ENTITY_TYPE, Identifier(FabAdditions.ID, name), entityType)
    }

    fun registerModEntities()
    {
        FabAdditions.logger.debug("Registering Mod Entities for ${FabAdditions.ID}")
    }
}