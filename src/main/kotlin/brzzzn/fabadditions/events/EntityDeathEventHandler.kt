package brzzzn.fabadditions.events

import brzzzn.fabadditions.FabAdditions
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.server.network.ServerPlayerEntity

object EntityDeathEventHandler {
    fun onDeath(diedEntity: LivingEntity, damageSource: DamageSource) {
        val attacker = damageSource.attacker
            ?: return // We do not handle if the entity was not attacked

        if (attacker !is ServerPlayerEntity) {
            return
        }
    }
}