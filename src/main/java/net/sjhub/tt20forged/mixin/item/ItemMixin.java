package net.sjhub.tt20forged.mixin.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sjhub.tt20forged.TT20Forged;
import net.sjhub.tt20forged.util.TPSUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void onGetMaxUseTime(ItemStack p_41454_, CallbackInfoReturnable<Integer> cir) {
        int original = cir.getReturnValue();
        if (!TT20Forged.config.enabled() || !TT20Forged.config.eatingAcceleration() || original == 0) return;
        cir.setReturnValue(TPSUtil.tt20(original, true));
    }
}
