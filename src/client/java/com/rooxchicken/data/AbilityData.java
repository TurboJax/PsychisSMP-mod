package com.rooxchicken.data;

import net.minecraft.util.Identifier;

public class AbilityData {
    public Identifier texture1;
    public Identifier texture2;
    public Identifier cooldownTexture = Identifier.of("psychis-mod", "textures/cooldown.png");
    public Identifier outlineTexture = Identifier.of("psychis-mod", "textures/empty.png");
    public int cooldown1 = 0;
    public int cooldown2 = 0;
    public int cooldown1Max = 1;
    public int cooldown2Max = 1;
    public boolean secondLocked = true;

    public AbilityData(String name) {
        if (name.equals("empty")) {
            this.texture1 = Identifier.of("psychis-mod", "textures/gui/unlock.png");
            this.texture2 = Identifier.of("psychis-mod", "textures/gui/unlock.png");
        } else {
            this.texture1 = Identifier.of("psychis-mod", "textures/abilities/" + name + "_0.png");
            this.texture2 = Identifier.of("psychis-mod", "textures/abilities/" + name + "_1.png");
        }
    }
}
