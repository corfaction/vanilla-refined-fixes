package net.corfaction.vanillarefined.fixes.client.mixin.arrow.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.corfaction.vanillarefined.fixes.client.arrow.layer.LivingEntityArrowLayer;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MobRenderer.class)
public abstract class MobRendererMixin<T extends Mob, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @SuppressWarnings({"unchecked"})
    @Inject(method = "<init>", at = @At("TAIL"))
    private void arrows_fix$addArrowLayer(
            EntityRendererProvider.Context context,
            M model,
            float shadow,
            CallbackInfo ci
    ) {
        ((LivingEntityRendererInvoker<S, M>) this)
                .arrows_fix$addLayer(
                        new LivingEntityArrowLayer<>(
                                (LivingEntityRenderer<T, S, M>) (Object) this,
                                context
                        )
                );
    }
}
