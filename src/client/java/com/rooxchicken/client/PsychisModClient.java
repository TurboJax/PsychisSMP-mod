package com.rooxchicken.client;

import com.rooxchicken.PsychisMod;
import com.rooxchicken.data.AbilityData;
import com.rooxchicken.data.AbilityDesc;
import com.rooxchicken.data.HandleData;
import com.rooxchicken.keybinding.CommandKeybind;
import com.rooxchicken.keybinding.Keybind;
import com.rooxchicken.screen.AbilityWidget;
import com.rooxchicken.screen.ConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PsychisModClient implements ClientModInitializer {
    public ArrayList<Keybind> keybinds;
    private final Category category = Category.create(Identifier.of("psychis-mod", "psychis"));
    public static int playerAbility = -1;
    public static AbilityData abilityData = new AbilityData("empty");
    public static ArrayList<AbilityDesc> abilities;
    public static AbilityWidget abilityElement1;
    public static AbilityWidget abilityElement2;
    public static boolean enabled = false;

    public void onInitializeClient() {
        abilities = new ArrayList<>();

        // Getting the keybinds into a list
        this.keybinds = new ArrayList<>();
        this.keybinds.add(new CommandKeybind(this.category, "key.ckb.ability1", GLFW.GLFW_KEY_Z, "hdn_ability1"));
        this.keybinds.add(new CommandKeybind(this.category, "key.ckb.ability2", GLFW.GLFW_KEY_X, "hdn_ability2"));
        this.keybinds.add(new Keybind(this.category, "key.ckb.config", GLFW.GLFW_KEY_C, () -> MinecraftClient.getInstance().setScreen(new ConfigScreen(Text.of("Config Screen")))));

        // Registering chat interceptor
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            String content = message.getString();
            if (content.startsWith("psyz91")) {
                HandleData.parseData(content);
                return false;
            }
            return true;
        });

        // Registering keybind listener
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (PsychisModClient.playerAbility != -1) {
                for (Keybind bind : keybinds) {
                    bind.checkKey();
                }
            }
        });

        // Registering events to clear ability data when a player joins/leaves a server
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            enabled = false;
            playerAbility = -2;
            abilityData = new AbilityData("empty");
        });
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            enabled = false;
            playerAbility = -2;
            abilityData = new AbilityData("empty");
        });


        // Initializing the hud elements
        abilityElement1 = new AbilityWidget(0);
        abilityElement2 = new AbilityWidget(1);
        HudElementRegistry.addLast(Identifier.of("psychis-mod", "ability_element1"), abilityElement1);
        HudElementRegistry.addLast(Identifier.of("psychis-mod", "ability_element2"), abilityElement2);

        // Loading the config
        load();
    }

    /**
     * Helper function that sends commands to the plugin through chat.
     * Only sends commands if the plugin has reached out to the client.
     *
     * @param msg The command to send
     */
    public static void sendChatCommand(String msg) {
        if (enabled) {
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand(msg);
        }
    }

    /**
     * Loads the config.  It just contains the positions and sizes of the AbilityWidgets.
     */
    public static void load() {
        File file = new File("psychis-mod.cfg");
        if (!file.exists()) {
            save();
            return;
        }

        try {
            Scanner scan = new Scanner(file);
            abilityElement1.load(scan);
            abilityElement2.load(scan);
            scan.close();
        } catch (FileNotFoundException var2) {
            PsychisMod.LOGGER.error("Failed to open config file.", var2);
        }
    }

    /**
     * Saves data to the config.
     */
    public static void save() {
        File file = new File("psychis-mod.cfg");

        try {
            FileWriter write = new FileWriter(file);
            write.write(abilityElement1.save() + abilityElement2.save());
            write.close();
        } catch (IOException var2) {
            PsychisMod.LOGGER.error("Failed to save config file.", var2);
        }
    }
}