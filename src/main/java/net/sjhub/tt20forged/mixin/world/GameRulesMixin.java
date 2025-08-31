package net.sjhub.tt20forged.mixin.world;

import net.minecraft.world.level.GameRules;
import net.sjhub.tt20forged.TT20Forged;
import net.sjhub.tt20forged.util.TPSCalculator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRules.class)
public class GameRulesMixin {

    @Inject(method = "getInt", at = @At("RETURN"), cancellable = true)
    private void randomTickSpeedAcceleration(GameRules.Key<GameRules.IntegerValue> rule, CallbackInfoReturnable<Integer> cir) {
        if (!TT20Forged.config.enabled() || !TT20Forged.config.randomTickSpeedAcceleration()) return;
        if (rule != GameRules.RULE_RANDOMTICKING) return;

        int original = cir.getReturnValue();
        cir.setReturnValue((int) (original * TPSCalculator.MAX_TPS / (float) TT20Forged.TPS_CALCULATOR.getMostAccurateTPS()));
    }
}
