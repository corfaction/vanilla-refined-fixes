package net.corfaction.vanillarefined.fixes.client.arrow.layer;

import net.corfaction.vanillarefined.fixes.client.arrow.api.LivingEntityRenderStateData;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.ArrowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;

public class LivingEntityArrowLayer<
        T extends LivingEntity,
        S extends LivingEntityRenderState,
        M extends EntityModel<? super S>>
        extends LivingEntityStuckInBodyLayer<S, M, ArrowRenderState> {

    public LivingEntityArrowLayer(
            LivingEntityRenderer<T, S, M> renderer,
            EntityRendererProvider.Context context
    ) {
        super(
                renderer,
                new ArrowModel(context.bakeLayer(ModelLayers.ARROW)),
                new ArrowRenderState(),
                TippableArrowRenderer.NORMAL_ARROW_LOCATION,
                StuckInBodyLayer.PlacementStyle.IN_CUBE
        );
    }

    @Override
    protected int numStuck(S state) {
        return ((LivingEntityRenderStateData) state)
                .arrows_fix$getArrowCount();
    }
}