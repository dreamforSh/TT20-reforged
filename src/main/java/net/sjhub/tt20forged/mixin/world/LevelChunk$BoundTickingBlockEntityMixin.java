package net.sjhub.tt20forged.mixin.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.registries.ForgeRegistries;
import net.sjhub.tt20forged.TT20Forged;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelChunk.BoundTickingBlockEntity.class)
public abstract class LevelChunk$BoundTickingBlockEntityMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntityTicker;tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;)V"))
    private <T extends BlockEntity> void onTick(BlockEntityTicker<T> instance, Level level, BlockPos blockPos, BlockState blockState, T t) {
        instance.tick(level, blockPos, blockState, t);
        if (!TT20Forged.config.enabled()) return;
        if (!TT20Forged.config.blockEntityAcceleration()) return;
        if (level.isClientSide()) return;
        if (!TT20Forged.blockEntityMaskConfig.getMask().isOkay(ForgeRegistries.BLOCKS.getKey(blockState.getBlock()))) return;

        int ticksToApply = TT20Forged.TPS_CALCULATOR.applicableMissedTicks();
        int cap = TT20Forged.config.getTickRepeatCap();
        if (cap > 0) {
            ticksToApply = Math.min(ticksToApply, cap);
        }

        for (int i = 0; i < ticksToApply; i++) {
            instance.tick(level, blockPos, blockState, t);
        }
    }
}
