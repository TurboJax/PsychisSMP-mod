package com.rooxchicken.screen;

import com.rooxchicken.PsychisMod;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    public ConfigScreen(Text title) {
        super(title);
    }

    public void init() {
        this.addDrawableChild(PsychisMod.abilityElement1);
        this.addDrawableChild(PsychisMod.abilityElement2);

        ButtonWidget resetButton = ButtonWidget.builder(Text.of("Reset"), (button) -> {
            PsychisMod.abilityElement1.reset();
            PsychisMod.abilityElement2.reset();
        }).dimensions(this.width / 2 - 50, this.height - 30, 100, 20).build();
        this.addDrawableChild(resetButton);
    }

    public boolean mouseReleased(Click click) {
        PsychisMod.save();
        return super.mouseReleased(click);
    }
}