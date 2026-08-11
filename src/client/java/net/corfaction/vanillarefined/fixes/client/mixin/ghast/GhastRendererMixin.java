package net.corfaction.vanillarefined.fixes.client.mixin.ghast;

import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.client.renderer.entity.state.GhastRenderState;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GhastRenderer.class)
public class GhastRendererMixin {

    @Unique
    private float currentScale = 1.0f;

    @Unique
    private float previousScale = 1.0f;

    @Unique
    private static final float TARGET_SCALE = 1.2f; // максимальный размер при зарядке
    @Unique
    private static final float ANIMATION_SPEED = 0.005f; // скорость анимации

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/Ghast;Lnet/minecraft/client/renderer/entity/state/GhastRenderState;F)V",
            at = @At("TAIL"))
    private void vanilla_refined_fixes$onExtractRenderState(Ghast entity, GhastRenderState state, float partialTicks, CallbackInfo ci) {
        previousScale = currentScale;

        if (state.isCharging) {
            currentScale = Math.min(currentScale + ANIMATION_SPEED, TARGET_SCALE);
        } else {
            currentScale = Math.max(currentScale - ANIMATION_SPEED, 1.0f);
        }

        // Плавная интерполяция с учётом partialTicks
        float interpolatedScale = previousScale + (currentScale - previousScale) * partialTicks;
        state.scale *= interpolatedScale;
    }
}
