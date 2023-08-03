package brzzzn.fabadditions.registry

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.block.entity.ChunkLoaderBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object FabBlockEntityRegistry
{
   val CHUNK_LOADER = registerBlockEntity(
       "chunk_loader",
       FabricBlockEntityTypeBuilder.create(::ChunkLoaderBlockEntity, FabBlockRegistry.CHUNK_LOADER).build(null))

    private fun <T : BlockEntity?> registerBlockEntity(name: String, entityType: BlockEntityType<T>): BlockEntityType<T>
    {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier(FabAdditions.ID, name), entityType)
    }

    fun registerModBlockEntities()
    {
        FabAdditions.logger.debug("Registering Mod BlockEntities for " + FabAdditions.ID)
    }
}