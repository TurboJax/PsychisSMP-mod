package com.rooxchicken.keybinding;

import com.rooxchicken.client.PsychisModClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.ArrayList;

public class KeyInputHandler {
    public static void registerKeyInputs(ArrayList<Keybind> bindings) {
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (PsychisModClient.playerAbility != -1) {
                for (Keybind bind : bindings) {
                    bind.checkKey();
                }
            }
        });
    }
}
