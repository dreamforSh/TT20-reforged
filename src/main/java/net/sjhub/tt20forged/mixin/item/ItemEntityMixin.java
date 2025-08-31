package net.sjhub.tt20forged.mixin.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.sjhub.tt20forged.TT20Forged;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow private int pickupDelay;

    @Inject(method = "tick", at = @At("HEAD"))
    private void pickupDelayTT20(CallbackInfo ci) {
        if (!TT20Forged.config.enabled() || !TT20Forged.config.pickupAcceleration()) return;
        if (((Entity) (Object) this).getLevel().isClientSide()) return;

        if (this.pickupDelay == 0) return;

        int ticksToApply = TT20Forged.TPS_CALCULATOR.applicableMissedTicks();
        int cap = TT20Forged.config.getTickRepeatCap();
        if (cap > 0) {
            ticksToApply = Math.min(ticksToApply, cap);
        }

        if (this.pickupDelay - ticksToApply <= 0) {
            this.pickupDelay = 0;
            return;
        }

        this.pickupDelay = this.pickupDelay - ticksToApply;
    }
}
