package com.rooxchicken.keybinding;

import com.rooxchicken.PsychisMod;
import net.minecraft.client.option.KeyBinding;
import org.jetbrains.annotations.Nullable;

public class CommandKeybind extends Keybind {
    public CommandKeybind(KeyBinding.Category category, String name, int GLFWkey, @Nullable String command) {
        super(category, name, GLFWkey, () -> PsychisMod.sendChatCommand(command + "_srt"), () -> PsychisMod.sendChatCommand(command + "_rpt"), () -> PsychisMod.sendChatCommand(command + "_end"));
    }
}
