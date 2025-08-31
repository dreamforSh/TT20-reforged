package net.sjhub.tt20forged.mixin.fluid;

import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.WaterFluid;
import net.sjhub.tt20forged.TT20Forged;
import net.sjhub.tt20forged.util.TPSUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFluid.class)
public abstract class WaterFluidMixin {

    @Inject(method = "getTickDelay", at = @At("RETURN"), cancellable = true)
    private void tickRateTT20(LevelReader p_76226_, CallbackInfoReturnable<Integer> cir) {
        if (!TT20Forged.config.enabled() || !TT20Forged.config.fluidAcceleration()) return;
        int original = cir.getReturnValue();
        cir.setReturnValue(TPSUtil.tt20(original, true));
    }
}
