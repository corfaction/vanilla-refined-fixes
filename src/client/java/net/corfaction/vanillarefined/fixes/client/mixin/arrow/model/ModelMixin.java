package net.corfaction.vanillarefined.fixes.client.mixin.arrow.model;

import net.corfaction.vanillarefined.fixes.client.arrow.api.RootModelProvider;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Model.class)
public abstract class ModelMixin implements RootModelProvider {

    @Shadow
    public abstract ModelPart root();

    @Override
    public ModelPart arrows_fix$getRoot() {
        return this.root();
    }
}
