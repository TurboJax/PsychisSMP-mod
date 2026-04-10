package com.rooxchicken.data;

import net.minecraft.client.MinecraftClient;
import com.rooxchicken.client.PsychisModClient;
import com.rooxchicken.screen.AbilitySelection;
import com.rooxchicken.screen.CardSelect;
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
                PsychisModClient.playerAbility = Integer.parseInt(data[2]);
                PsychisModClient.abilityData = new AbilityData(String.valueOf(PsychisModClient.playerAbility));
                PsychisModClient.abilityData.secondLocked = !Boolean.parseBoolean(data[3]);
                PsychisModClient.sendChatCommand("hdn_verifymod");
                break;
            case 1:
                int ability = Integer.parseInt(data[2]);
                int cooldown = Integer.parseInt(data[3]);
                int cooldownMax = Integer.parseInt(data[4]);
                if (ability == 0) {
                    PsychisModClient.abilityData.cooldown1 = cooldown;
                    PsychisModClient.abilityData.cooldown1Max = cooldownMax;
                }

                if (ability == 1) {
                    PsychisModClient.abilityData.cooldown2 = cooldown;
                    PsychisModClient.abilityData.cooldown2Max = cooldownMax;
                }
                break;
            case 2:
                AbilityDesc desc = new AbilityDesc();
                desc.name = data[2];
                if (desc.name.equals(".")) {
                    PsychisModClient.abilities.clear();
                    return;
                }

                desc.index = Integer.parseInt(data[3]);
                desc.passive = data[4];
                desc.ability1Name = data[5];
                desc.ability1Desc = data[6];
                desc.ability2Name = data[7];
                desc.ability2Desc = data[8];
                desc.secondUnlock = data[9];
                PsychisModClient.abilities.add(desc);
                break;
            case 3:
                PsychisModClient.playerAbility = -2;
                PsychisModClient.abilityData = new AbilityData("empty");
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
                PsychisModClient.enabled = true;
        }

    }
}
