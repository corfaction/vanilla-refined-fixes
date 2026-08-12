package net.corfaction.vanillarefined.fixes.client.mixin.ghast;

import net.corfaction.vanillarefined.fixes.client.ghast.GhastAnimationState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.client.renderer.entity.state.GhastRenderState;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.WeakHashMap;

@Environment(EnvType.CLIENT)
@Mixin(GhastRenderer.class)
public class GhastRendererMixin {

    @Unique
    private final Map<Ghast, GhastAnimationState> vanillaRefined$animationStates =
            new WeakHashMap<>();

    @Unique
    private GhastAnimationState vanillaRefined$getAnimationState(Ghast ghast) {
        return vanillaRefined$animationStates.computeIfAbsent(
                ghast,
                ignored -> new GhastAnimationState()
        );
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/monster/Ghast;Lnet/minecraft/client/renderer/entity/state/GhastRenderState;F)V",
            at = @At("TAIL")
    )
    private void vanillaRefined$onExtractRenderState(
            Ghast entity,
            GhastRenderState state,
            float partialTicks,
            CallbackInfo ci
    ) {
        GhastAnimationState animation =
                vanillaRefined$getAnimationState(entity);

        animation.previousScale = animation.currentScale;

        if (state.isCharging) {

            animation.recoil = 0.0f;

            animation.currentScale = Math.min(
                    animation.currentScale + GhastAnimationState.CHARGE_SPEED,
                    GhastAnimationState.CHARGE_SCALE
            );

        }

        else if (animation.wasCharging) {

            animation.recoil = 1.0f;

        }

        if (animation.recoil > 0.0f && !state.isCharging) {

            animation.recoil = Math.max(
                    animation.recoil - GhastAnimationState.RECOIL_SPEED,
                    0.0f
            );

            float recoilProgress = animation.recoil;

            animation.currentScale =
                    GhastAnimationState.NORMAL_SCALE
                            + (GhastAnimationState.RECOIL_SCALE
                            - GhastAnimationState.NORMAL_SCALE)
                            * recoilProgress;

        }

        else if (!state.isCharging && animation.recoil <= 0.0f) {

            animation.currentScale = Math.min(
                    animation.currentScale + GhastAnimationState.CHARGE_SPEED,
                    GhastAnimationState.NORMAL_SCALE
            );
        }

        animation.wasCharging = state.isCharging;

        float interpolatedScale =
                animation.previousScale
                        + (animation.currentScale - animation.previousScale)
                        * partialTicks;

        state.scale *= interpolatedScale;
    }
}
