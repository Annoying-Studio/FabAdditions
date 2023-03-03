package brzzzn.fabadditions.block.entity

import brzzzn.fabadditions.registry.FabBlockEntityRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World


class ChunkLoaderBlockEntity(pos: BlockPos?, state: BlockState?) : BlockEntity(FabBlockEntityRegistry.CHUNK_LOADER, pos, state)
{
    fun load(world: World, load: Boolean)
    {
        val chunkPos = ChunkPos(pos)
        (world as ServerWorld).setChunkForced(chunkPos.x, chunkPos.z, load)
    }
}