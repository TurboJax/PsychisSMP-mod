package com.rooxchicken.data;

import com.rooxchicken.PsychisMod;
import com.rooxchicken.screen.AbilitySelection;
import com.rooxchicken.screen.CardSelect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class HandleData {
    public static ArrayList<String> silentPlayers = new ArrayList<>();
    public static int dragonEggAbilityOverride = -1;

    public static void parseData(String msg) {
        String[] data = msg.split("_");
        int mode = Integer.parseInt(data[1]);
        switch (mode) {
            case 0:
                PsychisMod.playerAbility = Integer.parseInt(data[2]);
                PsychisMod.abilityData = new AbilityData(String.valueOf(PsychisMod.playerAbility));
                PsychisMod.abilityData.secondLocked = !Boolean.parseBoolean(data[3]);
                PsychisMod.sendChatCommand("hdn_verifymod");
                break;
            case 1:
                int ability = Integer.parseInt(data[2]);
                int cooldown = Integer.parseInt(data[3]);
                int cooldownMax = Integer.parseInt(data[4]);
                PsychisMod.abilityData.cooldowns.set(ability, cooldown);
                PsychisMod.abilityData.cooldownMaxes.set(ability, cooldownMax);
                break;
            case 2:
                AbilityDesc desc = new AbilityDesc();
                desc.name = data[2];
                if (desc.name.equals(".")) {
                    PsychisMod.abilities.clear();
                    return;
                }

                desc.index = Integer.parseInt(data[3]);
                desc.passive = data[4];
                desc.ability1Name = data[5];
                desc.ability1Desc = data[6];
                desc.ability2Name = data[7];
                desc.ability2Desc = data[8];
                desc.secondUnlock = data[9];
                PsychisMod.abilities.add(desc);
                break;
            case 3:
                PsychisMod.playerAbility = -2;
                PsychisMod.abilityData = new AbilityData("empty");
                MinecraftClient client = MinecraftClient.getInstance();
                client.setScreen(new AbilitySelection(Text.of("Ability Selection")));
                break;
            case 4:
                String username = data[2].replace("~", "_");
                if (Integer.parseInt(data[3]) == 0) {
                    if (!silentPlayers.contains(username)) {
                        silentPlayers.add(username);
                    }
                } else {
                    silentPlayers.remove(username);
                }
                break;
            case 5:
                MinecraftClient.getInstance().setScreen(new CardSelect(Text.of("Card Selection"), new int[]{Integer.parseInt(data[2]), Integer.parseInt(data[4]), Integer.parseInt(data[6])}, new String[]{data[3], data[5], data[7]}));
                break;
            case 6:
                dragonEggAbilityOverride = Integer.parseInt(data[2]);
                break;
            case 8:
                PsychisMod.enabled = true;
        }

    }
}
