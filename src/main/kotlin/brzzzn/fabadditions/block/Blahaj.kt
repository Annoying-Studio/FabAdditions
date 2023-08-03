package brzzzn.fabadditions.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

class Blahaj(settings: Settings?) : HorizontalFacingBlock(settings)
{
    override fun appendProperties(stateManager: StateManager.Builder<Block?, BlockState?>)
    {
        stateManager.add(Properties.HORIZONTAL_FACING)
    }

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, ctx: ShapeContext?): VoxelShape?
    {
        return when (state.get(FACING))
        {
            Direction.NORTH -> VoxelShapes.cuboid(0.25, 0.0, 0.0, 0.75, 0.45, 1.0)
            Direction.SOUTH -> VoxelShapes.cuboid(0.25, 0.0, 0.0, 0.75, 0.451, 1.0)
            Direction.EAST -> VoxelShapes.cuboid(0.0, 0.0, 0.25, 1.0, 0.45, 0.75)
            Direction.WEST -> VoxelShapes.cuboid(0.0, 0.0, 0.25, 1.0, 0.451, 0.75)
            else -> VoxelShapes.fullCube()
        }
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState
    {
        return defaultState.with(Properties.HORIZONTAL_FACING, ctx.playerLookDirection.opposite) as BlockState
    }
}