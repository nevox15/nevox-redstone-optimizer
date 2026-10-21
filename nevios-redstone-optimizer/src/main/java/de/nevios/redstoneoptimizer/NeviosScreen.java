package de.nevios.redstoneoptimizer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/** Das Einstellungs-GUI (Standard: Taste Ü). */
public class NeviosScreen extends Screen {
    private static final int COL_W = 124;
    private static final int GAP = 6;
    private static final int ROW_H = 24;

    private final NeviosConfig config;
    private int blockLabelY;

    public NeviosScreen(NeviosConfig config) {
        super(Component.literal("Nevios Redstone Optimizer"));
        this.config = config;
    }

    private static Component label(String name, boolean on) {
        return Component.literal(name + ": ")
                .append(Component.literal(on ? "AN" : "AUS").withStyle(on ? ChatFormatting.GREEN : ChatFormatting.RED));
    }

    private void addToggle(int x, int y, int w, String name, BooleanSupplier getter, Consumer<Boolean> setter) {
        addRenderableWidget(Button.builder(label(name, getter.getAsBoolean()), button -> {
            boolean newValue = !getter.getAsBoolean();
            setter.accept(newValue);
            button.setMessage(label(name, newValue));
        }).bounds(x, y, w, 20).build());
    }

    @Override
    protected void init() {
        int totalW = COL_W * 3 + GAP * 2;
        int left = (this.width - totalW) / 2;
        int halfW = (totalW - GAP) / 2;
        int y = 30;

        // --- Allgemeine Schalter ---
        addToggle(left, y, halfW, "Optimierung", () -> config.enabled, v -> config.enabled = v);
        addToggle(left + halfW + GAP, y, halfW, "Partikel aus", () -> config.suppressParticles, v -> config.suppressParticles = v);
        y += ROW_H;
        addToggle(left, y, halfW, "Chunk-Updates sparen", () -> config.skipChunkRebuilds, v -> config.skipChunkRebuilds = v);
        addToggle(left + halfW + GAP, y, halfW, "Keine Sichtlöcher", () -> config.avoidHoles, v -> config.avoidHoles = v);
        y += ROW_H + 4;

        // --- Bauteile ---
        blockLabelY = y;
        y += 14;
        OptimizerGroup[] groups = OptimizerGroup.values();
        for (int i = 0; i < groups.length; i++) {
            OptimizerGroup group = groups[i];
            int col = i % 3;
            int row = i / 3;
            addToggle(left + col * (COL_W + GAP), y + row * ROW_H, COL_W, group.label,
                    () -> config.isGroupEnabled(group), v -> config.setGroupEnabled(group, v));
        }

        // --- Unten: Alle an / Alle aus / Fertig ---
        int bottomY = this.height - 28;
        addRenderableWidget(Button.builder(Component.literal("Alle an"), b -> setAll(true))
                .bounds(left, bottomY, COL_W, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Alle aus"), b -> setAll(false))
                .bounds(left + COL_W + GAP, bottomY, COL_W, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Fertig"), b -> this.onClose())
                .bounds(left + (COL_W + GAP) * 2, bottomY, COL_W, 20).build());
    }

    private void setAll(boolean value) {
        for (OptimizerGroup group : OptimizerGroup.values()) {
            config.setGroupEnabled(group, value);
        }
        this.rebuildWidgets();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFFFF);
        graphics.drawCenteredString(this.font,
                Component.literal("Bauteile ohne Rendering, Partikel & Animation (wird beim Schließen angewendet):"),
                this.width / 2, blockLabelY, 0xFFAAAAAA);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    /** Beim Schließen: speichern, Zustand übernehmen, Welt neu rendern. */
    @Override
    public void removed() {
        config.save();
        NeviosState.rebuild(config);
        Minecraft mc = Minecraft.getInstance();
        if (mc.levelRenderer != null) {
            mc.levelRenderer.allChanged();
        }
    }
}
