package net.corfaction.vanillarefined.fixes.client.mixin.arrow.render;

import net.corfaction.vanillarefined.fixes.client.arrow.api.LivingEntityRenderStateData;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements LivingEntityRenderStateData {
    @Unique
    public int arrowsFixArrowCount;

    @Unique
    public int arrowsFixId;

    @Override
    public int arrows_fix$getId() {
        return arrowsFixId;
    }

    @Override
    public int arrows_fix$getArrowCount() {
        return arrowsFixArrowCount;
    }

    @Override
    public void arrows_fix$setId(int id) {
        this.arrowsFixId = id;
    }

    @Override
    public void arrows_fix$setArrowCount(int arrowCount) {
        this.arrowsFixArrowCount = arrowCount;
    }
}
