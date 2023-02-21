package brzzzn.fabadditions.entities.arrow

import brzzzn.fabadditions.registry.FabEntityRegistry
import brzzzn.fabadditions.registry.FabItemsRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World



class CopperArrowEntity : PersistentProjectileEntity
{
    constructor(entityType: EntityType<out CopperArrowEntity?>?, world: World?) : super(entityType as EntityType<out PersistentProjectileEntity?>?, world)
    constructor(x:Double, y:Double, z:Double, world: World) : super(FabEntityRegistry.COPPER_ARROW, x,y,z,world)
    constructor(owner: LivingEntity, world: World) : super(FabEntityRegistry.COPPER_ARROW, owner,world)

    private val lightningSpawnChance:Int = 50;

    override fun initDataTracker()
    {
        super.initDataTracker()
    }

    override fun asItemStack(): ItemStack
    {
        return ItemStack(FabItemsRegistry.COPPER_ARROW)
    }

    override fun tick()
    {
        super.tick()
        if (world.isClient)
        {
            if (inGround)
            {
                if (inGroundTime % 5 == 0)
                {
                    spawnParticles(1)
                }
            }
            else
            {
                spawnParticles(2)
            }
        }
        else if (inGround && inGroundTime != 0 && inGroundTime >= 600)
        {
            world.sendEntityStatus(this, 0.toByte())
        }
    }

    private fun spawnParticles(amount: Int)
    {
        val d = (16 and 0xFF).toDouble() / 255.0
        val e = (8 and 0xFF).toDouble() / 255.0
        val f = 0.toDouble() / 255.0
        for (j in 0 until amount)
        {
            world.addParticle(
                ParticleTypes.ELECTRIC_SPARK,
                getParticleX(0.5), this.randomBodyY, getParticleZ(0.5), d, e, f
            )
        }
    }

    override fun onHit(target: LivingEntity?)
    {
        super.onHit(target)
        if (target != null)
        {
            trySpawnLightning(target.blockPos)
        }
    }

    private fun trySpawnLightning(blockPos: BlockPos)
    {
        val rand = Random.createLocal().nextInt(100)
        if(rand > lightningSpawnChance)
        {
            if (world.isThundering && this.world.isSkyVisible(blockPos))
            {
                val lightning = EntityType.LIGHTNING_BOLT.create(world)
                lightning?.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos))
                world.spawnEntity(lightning)
                this.discard()
            }
        }
    }
}