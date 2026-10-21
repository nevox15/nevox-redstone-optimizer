package de.nevios.redstoneoptimizer.mixin;

import de.nevios.redstoneoptimizer.NeviosState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Unterdrückt Redstone-Staub- und Rauchpartikel an/neben optimierten Bauteilen (z.B. Werfer-Rauch). */
@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {
    @Inject(method = "createParticle", at = @At("HEAD"), cancellable = true, require = 0)
    private void nevios$noParticles(ParticleOptions options, double x, double y, double z,
                                    double xd, double yd, double zd, CallbackInfoReturnable<Particle> cir) {
        if (NeviosState.shouldSuppressParticle(options, x, y, z)) {
            cir.setReturnValue(null);
        }
    }
}
