package de.nevios.redstoneoptimizer;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeviosRedstoneOptimizerClient implements ClientModInitializer {
    public static final String MOD_ID = "nevios_redstone_optimizer";
    public static final Logger LOGGER = LoggerFactory.getLogger("Nevios Redstone Optimizer");

    public static NeviosConfig config;
    private static KeyMapping openGuiKey;

    @Override
    public void onInitializeClient() {
        config = NeviosConfig.load();
        NeviosState.rebuild(config);

        KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));

        // GLFW kennt die Taste am Platz von "[" (US-Layout). Auf einer deutschen Tastatur ist das die Taste "Ü".
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.nevios_redstone_optimizer.open_gui",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_BRACKET,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new NeviosScreen(config));
                }
            }
        });

        LOGGER.info("Nevios Redstone Optimizer geladen.");
    }
}
