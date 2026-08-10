package net.corfaction.vanillarefined.fixes.mixin.arrow.accessor;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker("isOnFire")
    boolean arrows_fix$invokeIsOnFire();
}
