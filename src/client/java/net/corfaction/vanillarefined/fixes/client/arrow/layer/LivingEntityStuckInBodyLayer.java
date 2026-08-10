package net.corfaction.vanillarefined.fixes.client.arrow.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.corfaction.vanillarefined.fixes.client.arrow.api.LivingEntityRenderStateData;
import net.corfaction.vanillarefined.fixes.client.arrow.api.RootModelProvider;
import net.corfaction.vanillarefined.fixes.client.mixin.arrow.accessor.ModelPartAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class LivingEntityStuckInBodyLayer<R extends LivingEntityRenderState, M extends EntityModel<? super R>, S> extends RenderLayer<R, M> {
    private final Model<S> model;
    private final S modelState;
    private final Identifier texture;
    private final net.minecraft.client.renderer.entity.layers.StuckInBodyLayer.PlacementStyle placementStyle;

    public LivingEntityStuckInBodyLayer(
            final LivingEntityRenderer<?, R, M> renderer,
            final Model<S> model,
            final S modelState,
            final Identifier texture,
            final net.minecraft.client.renderer.entity.layers.StuckInBodyLayer.PlacementStyle placementStyle
    ) {
        super(renderer);
        this.model = model;
        this.modelState = modelState;
        this.texture = texture;
        this.placementStyle = placementStyle;
    }

    protected abstract int numStuck(final R state);

    private void submitStuckItem(
            final PoseStack poseStack,
            final SubmitNodeCollector submitNodeCollector,
            final int lightCoords,
            final float directionX,
            final float directionY,
            final float directionZ,
            final int outlineColor
    ) {
        float directionXZ = Mth.sqrt(directionX * directionX + directionZ * directionZ);
        float yRot = (float)(Math.atan2(directionX, directionZ) * 180.0F / (float)Math.PI);
        float xRot = (float)(Math.atan2(directionY, directionXZ) * 180.0F / (float)Math.PI);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(xRot));
        submitNodeCollector.submitModel(this.model, this.modelState, poseStack, this.texture, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, null);
    }

    public void submit(
            final PoseStack poseStack,
            final SubmitNodeCollector submitNodeCollector,
            final int lightCoords,
            final R state,
            final float yRot,
            final float xRot
    ) {
        int count = this.numStuck(state);
        if (count > 0) {
            RandomSource random = RandomSource.createThreadLocalInstance(((LivingEntityRenderStateData)state).arrows_fix$getId());

            for (int i = 0; i < count; i++) {
                poseStack.pushPose();

                if (!(this.getParentModel() instanceof RootModelProvider provider)) {
                    poseStack.popPose();
                    break;
                }

                ModelPart modelPart = arrows_fix$getRandomBodyPart(
                        getDirectChildren(provider.arrows_fix$getRoot()),
                        random
                );

                if(modelPart == null) {
                    poseStack.popPose();
                    break;
                }

                if(modelPart.isEmpty()) {
                    poseStack.popPose();
                    break;
                }

                ModelPart.Cube cube = modelPart.getRandomCube(random);
                modelPart.translateAndRotate(poseStack);
                float midX = random.nextFloat();
                float midY = random.nextFloat();
                float midZ = random.nextFloat();
                if (this.placementStyle == net.minecraft.client.renderer.entity.layers.StuckInBodyLayer.PlacementStyle.ON_SURFACE) {
                    int plane = random.nextInt(3);
                    switch (plane) {
                        case 0:
                            midX = snapToFace(midX);
                            break;
                        case 1:
                            midY = snapToFace(midY);
                            break;
                        default:
                            midZ = snapToFace(midZ);
                    }
                }

                poseStack.translate(
                        Mth.lerp(midX, cube.minX, cube.maxX) / 16.0F,
                        Mth.lerp(midY, cube.minY, cube.maxY) / 16.0F,
                        Mth.lerp(midZ, cube.minZ, cube.maxZ) / 16.0F
                );

                removeEntityScale(poseStack);

                this.submitStuckItem(
                        poseStack,
                        submitNodeCollector,
                        lightCoords,
                        -(midX * 2.0F - 1.0F),
                        -(midY * 2.0F - 1.0F),
                        -(midZ * 2.0F - 1.0F),
                        state.outlineColor
                );
                
                poseStack.popPose();
            }
        }
    }

    public static ModelPart arrows_fix$getRandomBodyPart(final List<ModelPart> parts, final RandomSource random) {
        return Util.getRandom(parts, random);
    }

    public static List<ModelPart> getDirectChildren(ModelPart root) {
        return new ArrayList<>(
                ((ModelPartAccessor)(Object)root)
                        .arrows_fix$getChildren()
                        .values()
        );
    }

    private void removeEntityScale(PoseStack poseStack) {
        Matrix4f matrix = poseStack.last().pose();

        Vector3f scale = new Vector3f();
        matrix.getScale(scale);

        poseStack.scale(
                1.0F / scale.x(),
                1.0F / scale.y(),
                1.0F / scale.z()
        );
    }

    private static float snapToFace(final float value) {
        return value > 0.5F ? 1.0F : 0.5F;
    }
}