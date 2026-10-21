package de.nevios.redstoneoptimizer.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import de.nevios.redstoneoptimizer.NeviosState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.PistonHeadRenderer;
import net.minecraft.client.renderer.blockentity.state.PistonHeadRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Kolben-Animation (bewegte Kolbenköpfe/Blöcke) nicht mehr zeichnen. */
@Mixin(PistonHeadRenderer.class)
public class PistonHeadRendererMixin {
    @Inject(method = "submit", at = @At("HEAD"), cancellable = true, require = 0)
    private void nevios$skipAnimation(PistonHeadRenderState state, PoseStack poseStack,
                                      SubmitNodeCollector collector, CameraRenderState cameraState, CallbackInfo ci) {
        if (NeviosState.isHidden(Blocks.MOVING_PISTON)) {
            ci.cancel();
        }
    }
}
