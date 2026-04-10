package com.rooxchicken.data;

import net.minecraft.util.Identifier;
import java.util.ArrayList;
import java.util.List;

public class AbilityData {
    public List<Identifier> textures = new ArrayList<>(2);
    public Identifier cooldownTexture = Identifier.of("psychis-mod", "textures/cooldown.png");
    public List<Integer> cooldowns = new ArrayList<>(List.of(0, 0));
    public List<Integer> cooldownMaxes = new ArrayList<>(List.of(1, 1));
    public boolean secondLocked = true;

    public AbilityData(String name) {
        if (name.equals("empty")) {
            textures.add(Identifier.of("psychis-mod", "textures/gui/unlock.png"));
            textures.add(Identifier.of("psychis-mod", "textures/gui/unlock.png"));
        } else {
            textures.add(Identifier.of("psychis-mod", "textures/abilities/" + name + "_0.png"));
            textures.add(Identifier.of("psychis-mod", "textures/abilities/" + name + "_1.png"));
        }
    }
}
