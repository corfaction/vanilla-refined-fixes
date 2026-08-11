package net.corfaction.vanillarefined.fixes.mixin.slime;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(
            method = "causeFallDamage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void vanilla_refined_fixes$cancelSlimeFallDamage(
            double fallDistance, float damageModifier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir
    ) {
        if ((Object) this instanceof Slime) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "push(Lnet/minecraft/world/entity/Entity;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void vanilla_refined_fixes$bounceOffSlime(final Entity entity, CallbackInfo ci) {

        if ((Object) this instanceof Slime) {

            Vec3 velocity = entity.getDeltaMovement();

            if (velocity.y < 0) {

                entity.setDeltaMovement(
                        velocity.x,
                        -velocity.y,
                        velocity.z
                );

                entity.fallDistance = 0;
                ci.cancel();
            }
        }
    }
}
