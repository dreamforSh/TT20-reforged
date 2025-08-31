package net.sjhub.tt20forged.mixin.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.WritableLevelData;
import net.sjhub.tt20forged.TT20Forged;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Redirect(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/WritableLevelData;getDayTime()J"))
    private long addMissingTicksToTime(WritableLevelData instance) {
        long original = instance.getDayTime();
        if (!TT20Forged.config.enabled() || !TT20Forged.config.timeAcceleration()) return original;

        int ticksToApply = TT20Forged.TPS_CALCULATOR.applicableMissedTicks();
        int cap = TT20Forged.config.getTickRepeatCap();
        if (cap > 0) {
            ticksToApply = Math.min(ticksToApply, cap);
        }

        return original + ticksToApply;
    }
}
