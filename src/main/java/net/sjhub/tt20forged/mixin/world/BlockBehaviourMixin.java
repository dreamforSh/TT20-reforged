package net.sjhub.tt20forged.mixin.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.sjhub.tt20forged.TT20Forged;
import net.sjhub.tt20forged.util.TPSCalculator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {

    @Inject(method = "getDestroyProgress", at = @At("RETURN"), cancellable = true)
    private void onBlockBreakingCalc(BlockState state, Player player, BlockGetter getter, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (!TT20Forged.config.enabled() || !TT20Forged.config.blockBreakingAcceleration()) return;
        if (player.getLevel().isClientSide()) return;

        float original = cir.getReturnValue();
        cir.setReturnValue(original * TPSCalculator.MAX_TPS / (float) TT20Forged.TPS_CALCULATOR.getMostAccurateTPS());
    }
}
