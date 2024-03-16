package brzzzn.fabadditions.entities.arrow

import brzzzn.fabadditions.registry.FabEntityRegistry
import brzzzn.fabadditions.registry.FabItemsRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent


class BombArrowEntity : PersistentProjectileEntity
{
    constructor(entityType: EntityType<out BombArrowEntity?>?, world: World?) : super(entityType as EntityType<out PersistentProjectileEntity?>?, world)
    constructor(x:Double, y:Double, z:Double, world: World) : super(FabEntityRegistry.BOMB_ARROW, x,y,z,world)
    constructor(owner: LivingEntity, world: World) : super(FabEntityRegistry.BOMB_ARROW, owner,world)

    private val explosionPower = 2.5f

    override fun initDataTracker()
    {
        super.initDataTracker()
    }

    override fun asItemStack(): ItemStack
    {
        return ItemStack(FabItemsRegistry.BOMB_ARROW)
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
        for (i in 0 until amount)
        {
            world.addParticle(
                ParticleTypes.SMALL_FLAME,
                getParticleX(0.5), this.randomBodyY, getParticleZ(0.5), d, e, f
            )
        }
    }

    override fun onHit(target: LivingEntity?)
    {
        super.onHit(target)
        if (target != null)
        {
            trySpawnExplosion()
        }
    }

    override fun onCollision(hitResult: HitResult?)
    {
        val type = hitResult?.type
        if (type == HitResult.Type.ENTITY)
        {
            onEntityHit(hitResult as EntityHitResult?)
        } else if (type == HitResult.Type.BLOCK)
        {
            trySpawnExplosion()
            onBlockHit(hitResult as BlockHitResult?)
        }
        if (type != HitResult.Type.MISS)
        {
            this.emitGameEvent(GameEvent.PROJECTILE_LAND, owner)
        }
    }

    private fun trySpawnExplosion()
    {
        world.createExplosion(this, this.x, this.y, this.z, explosionPower, World.ExplosionSourceType.NONE)
        this.discard()
    }
}