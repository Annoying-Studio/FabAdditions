package brzzzn.fabadditions.registry

import brzzzn.fabadditions.FabAdditions
import brzzzn.fabadditions.FabAdditions.Companion.logger
import brzzzn.fabadditions.entities.arrow.BombArrowEntity
import brzzzn.fabadditions.entities.arrow.CopperArrowEntity
import brzzzn.fabadditions.item.*
import brzzzn.fabadditions.item.phantomstaff.PhantomStaff
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.DispenserBlock
import net.minecraft.block.dispenser.ProjectileDispenserBehavior
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.math.Position
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.world.World


object FabItemsRegistry {

    //region default settings

    private val rareToolSettings: FabricItemSettings = FabricItemSettings()
            .maxCount(1)
            .rarity(Rarity.UNCOMMON)

    private val defaultArrowSettings: FabricItemSettings = FabricItemSettings()

    //endregion

    val COPPER_ARROW: CopperArrowItem = CopperArrowItem(defaultArrowSettings)

    val BOMB_ARROW: BombArrowItem = BombArrowItem(defaultArrowSettings)

    /**
     * Registers a simple item as a mod item
     */
    private fun registerItem(name: String, item: Item, tab: RegistryKey<ItemGroup>): Item {
        Registry.register(Registries.ITEM, Identifier(FabAdditions.ID, name), item)
        ItemGroupEvents.modifyEntriesEvent(tab)
            .register(ItemGroupEvents.ModifyEntries { content: FabricItemGroupEntries ->
                content.add(
                    item
                )
            })
        return item
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
                AmethystFeather(rareToolSettings),
                ItemGroups.TOOLS
        )
        // Interdimensional Feather
        registerItem(
                "interdimensional_feather",
                InterdimensionalFeather(rareToolSettings),
                ItemGroups.TOOLS
        )
        // Phantom staff
        registerItem(
                "phantom_staff",
                PhantomStaff(rareToolSettings),
                ItemGroups.TOOLS
        )
    }

    private fun registerProjectiles()
    {
        //Copper Arrow
        registerItem(
            "copper_arrow",
            COPPER_ARROW,
            ItemGroups.COMBAT
        )
        registerDispenserBlockBehavior(COPPER_ARROW)

        registerItem(
            "bomb_arrow",
            BOMB_ARROW,
            ItemGroups.COMBAT,)
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