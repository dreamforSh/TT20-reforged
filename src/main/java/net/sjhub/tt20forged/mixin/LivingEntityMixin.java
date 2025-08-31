package net.sjhub.tt20forged.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.sjhub.tt20forged.TT20Forged;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow protected abstract void tickEffects();

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;tickEffects()V"))
    private void fixPotionDelayTick(CallbackInfo ci) {
        if (!TT20Forged.config.enabled() || !TT20Forged.config.potionEffectAcceleration()) return;
        if (((Entity) (Object) this).getLevel().isClientSide()) return;

        int ticksToApply = TT20Forged.TPS_CALCULATOR.applicableMissedTicks();
        int cap = TT20Forged.config.getTickRepeatCap();
        if (cap > 0) {
            ticksToApply = Math.min(ticksToApply, cap);
        }

        for (int i = 0; i < ticksToApply; i++) {
            tickEffects();
        }
    }
}
