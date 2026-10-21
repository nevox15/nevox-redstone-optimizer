package de.nevios.redstoneoptimizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/** Einstellungen, gespeichert in config/nevios_redstone_optimizer.json */
public final class NeviosConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /** Hauptschalter der Mod. */
    public boolean enabled = true;
    /** Redstone-Staub-/Rauch-Partikel in der Nähe optimierter Bauteile unterdrücken. */
    public boolean suppressParticles = true;
    /** Keine Chunk-Neuberechnung, wenn sich nur der Zustand eines optimierten Bauteils ändert. */
    public boolean skipChunkRebuilds = true;
    /** Angrenzende Blöcke trotzdem rendern, damit keine "Löcher" entstehen. */
    public boolean avoidHoles = true;
    /** Pro Bauteil-Gruppe: optimieren ja/nein. */
    public Map<String, Boolean> groups = new LinkedHashMap<>();

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("nevios_redstone_optimizer.json");
    }

    public boolean isGroupEnabled(OptimizerGroup group) {
        Boolean value = groups.get(group.key);
        return value != null ? value : group.defaultEnabled;
    }

    public void setGroupEnabled(OptimizerGroup group, boolean value) {
        groups.put(group.key, value);
    }

    public static NeviosConfig load() {
        Path file = path();
        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file)) {
                NeviosConfig cfg = GSON.fromJson(reader, NeviosConfig.class);
                if (cfg != null) {
                    if (cfg.groups == null) {
                        cfg.groups = new LinkedHashMap<>();
                    }
                    return cfg;
                }
            } catch (Exception e) {
                NeviosRedstoneOptimizerClient.LOGGER.warn("Config konnte nicht gelesen werden, nutze Standardwerte.", e);
            }
        }
        NeviosConfig cfg = new NeviosConfig();
        cfg.save();
        return cfg;
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(path())) {
            GSON.toJson(this, writer);
        } catch (Exception e) {
            NeviosRedstoneOptimizerClient.LOGGER.warn("Config konnte nicht gespeichert werden.", e);
        }
    }
}
