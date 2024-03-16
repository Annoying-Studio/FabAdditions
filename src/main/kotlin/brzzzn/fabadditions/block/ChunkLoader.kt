package brzzzn.fabadditions.block

import brzzzn.fabadditions.block.entity.ChunkLoaderBlockEntity
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World


class ChunkLoader(settings: Settings?) : BlockWithEntity(settings), BlockEntityProvider
{
    override fun getRenderType(state: BlockState?): BlockRenderType?
    {
        return BlockRenderType.MODEL
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos?, oldState: BlockState, notify: Boolean)
    {
        if (state.block !== oldState.block)
        {
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is ChunkLoaderBlockEntity)
            {
                (blockEntity as ChunkLoaderBlockEntity?)?.load(world, true)
            }
        }
        super.onBlockAdded(state, world, pos, oldState, notify)
    }

    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean)
    {
        if (state.block !== newState.block)
        {
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is ChunkLoaderBlockEntity)
            {
                (blockEntity as ChunkLoaderBlockEntity?)?.load(world, false)
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity?
    {
        return ChunkLoaderBlockEntity(pos, state)
    }

    override fun appendTooltip(stack: ItemStack, world: BlockView?, tooltip: MutableList<Text>, options: TooltipContext)
    {
        if (Screen.hasShiftDown())
        {
            tooltip.add(Text.translatable("block.fabadditions.chunk_loader.tooltip").formatted(Formatting.GRAY))
        }
        else
        {
            tooltip.add(Text.translatable("tooltip.fabadditions.hold_shift").formatted(Formatting.GRAY))
        }
    }
}
