package com.rooxchicken.screen;

import com.rooxchicken.client.PsychisModClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    public ConfigScreen(Text title) {
        super(title);
    }

    public void init() {
        this.addDrawableChild(PsychisModClient.abilityElement1);
        this.addDrawableChild(PsychisModClient.abilityElement2);

        ButtonWidget resetButton = ButtonWidget.builder(Text.of("Reset"), (button) -> {
            PsychisModClient.abilityElement1.reset();
            PsychisModClient.abilityElement2.reset();
        }).dimensions(this.width / 2 - 50, this.height - 30, 100, 20).build();
        this.addDrawableChild(resetButton);
    }

    public boolean mouseReleased(Click click) {
        PsychisModClient.save();
        return super.mouseReleased(click);
    }
}