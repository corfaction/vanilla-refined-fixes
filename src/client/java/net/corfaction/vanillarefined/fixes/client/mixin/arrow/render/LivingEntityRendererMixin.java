package net.corfaction.vanillarefined.fixes.client.mixin.arrow.render;

import net.corfaction.vanillarefined.fixes.client.arrow.api.LivingEntityRenderStateData;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void vanilla_refined_fixes$onExtractRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        ((LivingEntityRenderStateData)state).vanilla_refined_fixes$setArrowCount(entity.getArrowCount());
        ((LivingEntityRenderStateData)state).vanilla_refined_fixes$setId(entity.getId());
    }
}
