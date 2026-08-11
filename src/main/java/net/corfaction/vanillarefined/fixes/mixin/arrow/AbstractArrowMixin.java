package net.corfaction.vanillarefined.fixes.mixin.arrow;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

    @Inject(method = "onHitBlock", at = @At("TAIL"))
    private void vanilla_refined_fixes$onHitBlock(final BlockHitResult hitResult, final CallbackInfo ci) {

        AbstractArrow arrow = (AbstractArrow) (Object) this;

        if (!arrow.isOnFire()) {
            return;
        }

        if (!(arrow.level() instanceof ServerLevel level)) {
            return;
        }

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        if (!CampfireBlock.canLight(state) && !CandleBlock.canLight(state) && !CandleCakeBlock.canLight(state)) {
            BlockPos relativePos = pos.relative(hitResult.getDirection());
            if (BaseFireBlock.canBePlacedAt(level, relativePos, hitResult.getDirection())) {
                BlockState fireState = BaseFireBlock.getState(level, relativePos);
                level.setBlock(relativePos, fireState, 11);
            }
        } else {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
        }

    }
}