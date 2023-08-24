package com.rmlogsClient;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class RmlogsKeybind {
    private static KeyBinding keyBinding;

    public static void registerKeybind() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.rmlogs.openGUI",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.rmlogs.keybind"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                client.setScreen(new RmlogsScreen(client.currentScreen));
                System.out.println("Works!");
            }
        });
    }
}
