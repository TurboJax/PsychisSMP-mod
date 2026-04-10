package com.rooxchicken.keybinding;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.Nullable;

public class Keybind {
    public KeyBinding binding;
    private boolean wasPressed = false;
    private final Runnable onPress;
    private final Runnable onRepeat;
    private final Runnable onRelease;

    public Keybind(Category category, String name, int GLFWkey, @Nullable Runnable onPress) {
        this(category, name, GLFWkey, onPress, null, null);
    }

    public Keybind(Category category, String name, int GLFWkey, @Nullable Runnable onPress, @Nullable Runnable onRepeat) {
        this(category, name, GLFWkey, onPress, onRepeat, null);
    }

    public Keybind(Category category, String name, int GLFWkey, @Nullable Runnable onPress, @Nullable Runnable onRepeat, @Nullable Runnable onRelease) {
        this.binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(name, InputUtil.Type.KEYSYM, GLFWkey, category));
        this.onPress = onPress == null ? () -> {
        } : onPress;
        this.onRepeat = onRepeat == null ? () -> {
        } : onRepeat;
        this.onRelease = onRelease == null ? () -> {
        } : onRelease;
    }

    public void checkKey() {
        if (this.binding.isPressed() && !this.wasPressed) {
            onPress.run();
            this.wasPressed = true;
        } else if (this.binding.isPressed()) {
            onRepeat.run();
        } else if (this.wasPressed) {
            this.wasPressed = false;
            onRelease.run();
        }
    }
}
