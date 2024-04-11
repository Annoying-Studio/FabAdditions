package brzzzn.fabadditions.mixin;

import brzzzn.fabadditions.FabAdditions;
import brzzzn.fabadditions.events.EntityDeathEventHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public abstract class EntityDamageMixin {
    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @SuppressWarnings("DataFlowIssue")
    @Inject(at = @At("TAIL"), method = "onDeath")
    private void Death(DamageSource damageSource, CallbackInfo info)
    {
        try {
            EntityDeathEventHandler.INSTANCE.onDeath((LivingEntity) (Object) this, damageSource);
        } catch (Exception e) {
            FabAdditions.Companion.getLogger().error("Error while handling entity death", e);
        }
    }
}
