package net.corfaction.vanillarefined.fixes.mixin.arrow.entity;

import net.corfaction.vanillarefined.fixes.mixin.arrow.accessor.EntityAccessor;
import net.minecraft.core.BlockPos;
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
    private void arrows_fix$onHitBlock(final BlockHitResult hitResult, final CallbackInfo ci) {

        AbstractArrow arrow = (AbstractArrow) (Object) this;

        if (!((EntityAccessor) this).arrows_fix$invokeIsOnFire()) {
            return;
        }

        if (!(arrow.level() instanceof net.minecraft.server.level.ServerLevel level)) {
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