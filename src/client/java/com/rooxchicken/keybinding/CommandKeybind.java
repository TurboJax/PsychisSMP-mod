package com.rooxchicken.keybinding;

import com.rooxchicken.client.PsychisModClient;
import net.minecraft.client.option.KeyBinding;
import org.jetbrains.annotations.Nullable;

public class CommandKeybind extends Keybind {
    public CommandKeybind(KeyBinding.Category category, String name, int GLFWkey, @Nullable String command) {
        super(category, name, GLFWkey, () -> PsychisModClient.sendChatCommand(command + "_srt"), () -> PsychisModClient.sendChatCommand(command + "_rpt"), () -> PsychisModClient.sendChatCommand(command + "_end"));
    }
}
