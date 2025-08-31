package net.sjhub.tt20forged.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.sjhub.tt20forged.TT20Forged;
import net.sjhub.tt20forged.util.TPSUtil;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "getPortalWaitTime", at = @At("RETURN"), cancellable = true)
    private void netherPortalTimeTT20(CallbackInfoReturnable<Integer> cir) {
        int original = cir.getReturnValue();
        if (!TT20Forged.config.enabled() || !TT20Forged.config.portalAcceleration()) return;
        if (((Entity) (Object) this).getLevel().isClientSide()) return;
        if (original == 1) return;
        cir.setReturnValue(TPSUtil.tt20(original, false));
    }

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Player;sleepCounter:I", opcode = Opcodes.GETFIELD))
    private int tickTT20(Player player) {
        int original = player.getSleepTimer();
        if (!TT20Forged.config.enabled() || !TT20Forged.config.sleepingAcceleration()) return original;
        if (((Entity) (Object) this).getLevel().isClientSide()) return original;

        int ticksToApply = TT20Forged.TPS_CALCULATOR.applicableMissedTicks();
        int cap = TT20Forged.config.getTickRepeatCap();
        if (cap > 0) {
            ticksToApply = Math.min(ticksToApply, cap);
        }

        return player.isSleeping() ? original + ticksToApply : original;
    }
}
