package brzzzn.fabadditions.registry

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.FabAdditions.Companion.logger
import brzzzn.fabadditions.entities.arrow.CopperArrowEntity
import brzzzn.fabadditions.item.AmethystFeather
import brzzzn.fabadditions.item.CopperArrowItem
import brzzzn.fabadditions.item.InterdimensionalFeather
import brzzzn.fabadditions.item.PhantomStaff
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.DispenserBlock
import net.minecraft.block.dispenser.ProjectileDispenserBehavior
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Position
import net.minecraft.util.registry.Registry
import net.minecraft.world.World


object FabItemsRegistry {

    //region default settings
    private val defaultToolSettings: FabricItemSettings = FabricItemSettings()
            .group(ItemGroup.TOOLS)
            .maxCount(1)

    private val defaultArrowSettings: FabricItemSettings = FabricItemSettings()
        .group(ItemGroup.COMBAT)
    //endregion

    //region Item Fields

    val COPPER_ARROW: CopperArrowItem = CopperArrowItem(defaultArrowSettings)

    //endregion

    /**
     * Registers a simple item as a mod item
     */
    private fun registerItem(name: String, item: Item): Item {
        return Registry.register(Registry.ITEM, Identifier(FabAdditions.ID, name), item)
    }

    /**
     * Registers all modded items
     */
    fun registerModItems() {
        logger.debug("Registering Mod Items for ${FabAdditions.ID}")
        // Call all registers
        registerTools()
        registerProjectiles()

        logger.debug("Done registering Mod Items for ${FabAdditions.ID}")
    }

    private fun registerTools() {
        // Amethyst Feather
        registerItem(
                "amethyst_feather",
                AmethystFeather(defaultToolSettings)
        )
        // Interdimensional Feather
        registerItem(
                "interdimensional_feather",
                InterdimensionalFeather(defaultToolSettings)
        )
        // Phantom staff
        registerItem(
                "phantom_staff",
                PhantomStaff(defaultToolSettings)
        )
    }

    private fun registerProjectiles()
    {
        //Copper Arrow
        registerItem(
            "copper_arrow",
            COPPER_ARROW
        )
        registerDispenserBlockBehavior(COPPER_ARROW)
    }

    private fun registerDispenserBlockBehavior(item: CopperArrowItem)
    {
        DispenserBlock.registerBehavior(item, object : ProjectileDispenserBehavior()
        {
            override fun createProjectile(world: World, position: Position, stack: ItemStack?): ProjectileEntity?
            {
                val arrowEntity = CopperArrowEntity(position.x, position.y, position.z, world)
                arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED

                return arrowEntity
            }
        })
    }
}