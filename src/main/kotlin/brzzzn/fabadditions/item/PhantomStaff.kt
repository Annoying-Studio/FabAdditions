package brzzzn.fabadditions.item

import brzzzn.fabadditions.guis.PhantomStaffGui
import brzzzn.fabadditions.screens.PhantomStaffScreen
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class PhantomStaff(settings: Settings) : Item(settings) {
    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        if (world?.isClient == true || hand != Hand.MAIN_HAND || user?.isSneaking == true) return super.use(world, user, hand)

        // Check that player is server player
        if (user !is ServerPlayerEntity) return super.use(world, user, hand)

        val players = hashSetOf<PlayerEntity>()

        user.server.worlds.forEach {
            players.addAll(it.players)
        }

//        // Remove self from set
//        players.removeIf {
//            it.uuid == user.uuid
//        }

        MinecraftClient.getInstance().execute {
            MinecraftClient.getInstance().setScreen(PhantomStaffScreen(PhantomStaffGui(players) { teleportToPlayer(user, it) }))
        }
        return super.use(world, user, hand)
    }

    private fun teleportToPlayer(user: PlayerEntity, player: PlayerEntity) {
        if (user !is ServerPlayerEntity) return

        user.teleport(user.server.getWorld(player.world.registryKey), player.x, player.y, player.z, player.headYaw, 0.5f)
    }

    override fun appendTooltip(stack: ItemStack?, world: World?, tooltip: MutableList<Text>?, context: TooltipContext?) {
        if (Screen.hasShiftDown()) {
            tooltip?.add(Text.translatable("item.fabadditions.phantom_staff.tooltip").formatted(Formatting.GRAY))
        } else {
            tooltip?.add(Text.translatable("global.fabadditions.tooltip").formatted(Formatting.GRAY))
        }
        super.appendTooltip(stack, world, tooltip, context)
    }
}