package de.nevios.redstoneoptimizer;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Alle Bauteile, die man im GUI ein- und ausschalten kann.
 * "key" wird in der Config gespeichert, "label" steht im GUI.
 */
public enum OptimizerGroup {
    OBSERVER("observer", "Observer", true, Blocks.OBSERVER),
    DISPENSER("dispenser", "Werfer (Dispenser)", true, Blocks.DISPENSER),
    DROPPER("dropper", "Spender (Dropper)", true, Blocks.DROPPER),
    REPEATER("repeater", "Verstärker (Repeater)", true, Blocks.REPEATER),
    COMPARATOR("comparator", "Komparator", true, Blocks.COMPARATOR),
    REDSTONE_WIRE("redstone_wire", "Redstone-Staub", true, Blocks.REDSTONE_WIRE),
    REDSTONE_TORCH("redstone_torch", "Redstone-Fackel", true, Blocks.REDSTONE_TORCH, Blocks.REDSTONE_WALL_TORCH),
    PISTON_ANIMATION("piston_animation", "Kolben-Animation", true, Blocks.MOVING_PISTON),
    PISTON("piston", "Kolben (Block)", false, Blocks.PISTON, Blocks.STICKY_PISTON, Blocks.PISTON_HEAD),
    REDSTONE_BLOCK("redstone_block", "Redstone-Block", false, Blocks.REDSTONE_BLOCK),
    REDSTONE_LAMP("redstone_lamp", "Redstone-Lampe", false, Blocks.REDSTONE_LAMP),
    HOPPER("hopper", "Trichter (Hopper)", false, Blocks.HOPPER),
    LEVER("lever", "Hebel", false, Blocks.LEVER),
    TARGET("target", "Zielblock", false, Blocks.TARGET),
    RAILS("rails", "Spezial-Schienen", false, Blocks.POWERED_RAIL, Blocks.DETECTOR_RAIL, Blocks.ACTIVATOR_RAIL);

    public final String key;
    public final String label;
    public final boolean defaultEnabled;
    public final Block[] blocks;

    OptimizerGroup(String key, String label, boolean defaultEnabled, Block... blocks) {
        this.key = key;
        this.label = label;
        this.defaultEnabled = defaultEnabled;
        this.blocks = blocks;
    }
}
