package net.corfaction.vanillarefined.fixes.mixin.slime;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class EntityMixin {
    @Redirect(
            method = "restituteMovementAfterCollisions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
            )
    )
    private void vanilla_refined_fixes$slimeBounce(
            Entity entity,
            Vec3 deltaMovement
    ) {
        if (entity instanceof Slime slime) {

            Vec3 currentMovement = slime.getDeltaMovement();

            if (currentMovement.y < -0.5) {
                entity.setDeltaMovement(
                        deltaMovement.x,
                        -currentMovement.y,
                        deltaMovement.z
                );
                return;
            }
        }

        entity.setDeltaMovement(deltaMovement);
    }
}
