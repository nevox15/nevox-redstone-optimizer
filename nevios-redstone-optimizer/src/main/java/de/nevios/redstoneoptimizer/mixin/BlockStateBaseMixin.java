package de.nevios.redstoneoptimizer.mixin;

import de.nevios.redstoneoptimizer.NeviosState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Macht optimierte Bauteile "unsichtbar": Der Chunk-Builder überspringt Blöcke mit RenderShape.INVISIBLE.
 * Funktioniert dadurch auch mit Sodium & Co., die diese Prüfung ebenfalls nutzen.
 */
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    @Shadow
    public abstract Block getBlock();

    @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true, require = 0)
    private void nevios$hide(CallbackInfoReturnable<RenderShape> cir) {
        if (NeviosState.isHidden(this.getBlock())) {
            cir.setReturnValue(RenderShape.INVISIBLE);
        }
    }

    /** Versteckte Vollblöcke (Observer, Werfer, ...) sollen Nachbarflächen nicht verdecken -> keine Löcher. */
    @Inject(method = "canOcclude", at = @At("HEAD"), cancellable = true, require = 0)
    private void nevios$noOcclude(CallbackInfoReturnable<Boolean> cir) {
        if (NeviosState.shouldNotOcclude(this.getBlock())) {
            cir.setReturnValue(false);
        }
    }
}
