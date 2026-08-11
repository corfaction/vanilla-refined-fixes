package net.corfaction.vanillarefined.fixes.client.mixin.arrow.render;

import net.corfaction.vanillarefined.fixes.client.arrow.api.LivingEntityRenderStateData;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements LivingEntityRenderStateData {
    @Unique
    public int vanilla_refined_fixes$arrowCount;

    @Unique
    public int vanilla_refined_fixes$id;

    @Override
    public int vanilla_refined_fixes$getId() {
        return vanilla_refined_fixes$id;
    }

    @Override
    public int vanilla_refined_fixes$getArrowCount() {
        return vanilla_refined_fixes$arrowCount;
    }

    @Override
    public void vanilla_refined_fixes$setId(int id) {
        this.vanilla_refined_fixes$id = id;
    }

    @Override
    public void vanilla_refined_fixes$setArrowCount(int arrowCount) {
        this.vanilla_refined_fixes$arrowCount = arrowCount;
    }
}
