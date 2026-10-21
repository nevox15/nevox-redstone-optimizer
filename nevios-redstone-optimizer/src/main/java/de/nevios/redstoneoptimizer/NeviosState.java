package de.nevios.redstoneoptimizer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Laufzeit-Zustand, den die Mixins abfragen. Absichtlich ohne Abhängigkeit zu Blocks/OptimizerGroup
 * im statischen Initialisierer, weil die Mixins sehr früh aufgerufen werden können.
 */
public final class NeviosState {
    private static final Direction[] DIRECTIONS = Direction.values();

    private static volatile boolean enabled = false;
    private static volatile boolean suppressParticles = false;
    private static volatile boolean skipRebuilds = false;
    private static volatile boolean avoidHoles = false;
    private static volatile Set<Block> hidden = Collections.emptySet();

    private NeviosState() {
    }

    public static void rebuild(NeviosConfig cfg) {
        Set<Block> set = Collections.newSetFromMap(new IdentityHashMap<>());
        for (OptimizerGroup group : OptimizerGroup.values()) {
            if (cfg.isGroupEnabled(group)) {
                Collections.addAll(set, group.blocks);
            }
        }
        suppressParticles = cfg.suppressParticles;
        skipRebuilds = cfg.skipChunkRebuilds;
        avoidHoles = cfg.avoidHoles;
        enabled = cfg.enabled;
        hidden = set;
    }

    /** Soll dieser Block nicht gerendert werden? */
    public static boolean isHidden(Block block) {
        return enabled && hidden.contains(block);
    }

    /** Soll dieser (versteckte) Block Nachbarflächen NICHT mehr verdecken? */
    public static boolean shouldNotOcclude(Block block) {
        return enabled && avoidHoles && hidden.contains(block);
    }

    /** Ändert sich nur der Zustand eines versteckten Blocks -> kein Chunk-Rebuild nötig. */
    public static boolean canSkipRebuild(BlockState oldState, BlockState newState) {
        if (!enabled || !skipRebuilds) {
            return false;
        }
        Block block = oldState.getBlock();
        return block == newState.getBlock() && hidden.contains(block);
    }

    /** Rauch-/Staub-Partikel an oder neben versteckten Bauteilen unterdrücken. */
    public static boolean shouldSuppressParticle(ParticleOptions options, double x, double y, double z) {
        if (!enabled || !suppressParticles || hidden.isEmpty()) {
            return false;
        }
        ParticleType<?> type = options.getType();
        if (type != ParticleTypes.DUST && type != ParticleTypes.SMOKE) {
            return false;
        }
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return false;
        }
        int bx = Mth.floor(x);
        int by = Mth.floor(y);
        int bz = Mth.floor(z);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        if (hidden.contains(level.getBlockState(pos.set(bx, by, bz)).getBlock())) {
            return true;
        }
        for (Direction dir : DIRECTIONS) {
            pos.set(bx + dir.getStepX(), by + dir.getStepY(), bz + dir.getStepZ());
            if (hidden.contains(level.getBlockState(pos).getBlock())) {
                return true;
            }
        }
        return false;
    }
}
