package de.nevios.redstoneoptimizer.mixin;

import de.nevios.redstoneoptimizer.NeviosState;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Spart die teuren Chunk-Neuberechnungen, wenn sich nur der Zustand eines versteckten Bauteils ändert. */
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(method = "blockChanged", at = @At("HEAD"), cancellable = true, require = 0)
    private void nevios$skipRebuild(BlockGetter level, BlockPos pos, BlockState oldState, BlockState newState,
                                    int flags, CallbackInfo ci) {
        if (NeviosState.canSkipRebuild(oldState, newState)) {
            ci.cancel();
        }
    }
}
