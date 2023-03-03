package brzzzn.fabadditions.registry

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.FabAdditions.Companion.logger
import brzzzn.fabadditions.entities.arrow.BombArrowEntity
import brzzzn.fabadditions.entities.arrow.CopperArrowEntity
import brzzzn.fabadditions.item.*
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.DispenserBlock
import net.minecraft.block.dispenser.ProjectileDispenserBehavior
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ArrowItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.math.Position
import net.minecraft.util.registry.Registry
import net.minecraft.world.World


object FabItemsRegistry {

    //region default settings
    private val rareToolSettings: FabricItemSettings = FabricItemSettings()
            .group(ItemGroup.TOOLS)
            .maxCount(1)
            .rarity(Rarity.UNCOMMON)

    private val defaultArrowSettings: FabricItemSettings = FabricItemSettings()
        .group(ItemGroup.COMBAT)
    //endregion

    val COPPER_ARROW: CopperArrowItem = CopperArrowItem(defaultArrowSettings)

    val BOMB_ARROW: BombArrowItem = BombArrowItem(defaultArrowSettings)

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
                AmethystFeather(rareToolSettings)
        )
        // Interdimensional Feather
        registerItem(
                "interdimensional_feather",
                InterdimensionalFeather(rareToolSettings)
        )
        // Phantom staff
        registerItem(
                "phantom_staff",
                PhantomStaff(rareToolSettings)
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

        registerItem("bomb_arrow",
            BOMB_ARROW)
        registerDispenserBlockBehavior(BOMB_ARROW)
    }

    private fun registerDispenserBlockBehavior(item: ArrowItem)
    {
        DispenserBlock.registerBehavior(item, object : ProjectileDispenserBehavior()
        {
            override fun createProjectile(world: World, position: Position, stack: ItemStack?): ProjectileEntity?
            {
                if(item is CopperArrowItem)
                {
                    val arrowEntity = CopperArrowEntity(position.x, position.y, position.z, world)
                    arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED

                    return arrowEntity
                }
                else if(item is BombArrowItem)
                {
                    val arrowEntity = BombArrowEntity(position.x, position.y, position.z, world)
                    arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED

                    return arrowEntity
                }
                else
                    return null
            }
        })
    }
}