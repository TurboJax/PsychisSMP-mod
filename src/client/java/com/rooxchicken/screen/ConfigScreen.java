package com.rooxchicken.screen;

import com.rooxchicken.client.PsychisModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ConfigScreen extends Screen {
    private int mouseStatus = -2;
    private ButtonWidget resetButton;
    public boolean ObjectSelected = false;

    public ConfigScreen(Text title) {
        super(title);
    }

    public void init() {
        this.resetButton = ButtonWidget.builder(Text.of("Reset"), (button) -> {
        this.addDrawableChild(PsychisModClient.abilityElement1);
        this.addDrawableChild(PsychisModClient.abilityElement2);

            PsychisModClient.abilityElement1.reset();
            PsychisModClient.abilityElement2.reset();
        }).dimensions(this.width / 2 - 50, this.height - 30, 100, 20).build();
        this.addDrawableChild(this.resetButton);
    }

    public boolean mouseReleased(Click click) {
        PsychisModClient.save();
        return super.mouseReleased(click);
    }

    public boolean keyPressed(KeyInput keyInput) {
        if (keyInput.key() == GLFW.GLFW_KEY_ESCAPE) {
            MinecraftClient.getInstance().setScreen(null);
            return true;
        }

        return super.keyPressed(keyInput);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        PsychisModClient.guiCallback.onHudRender(context, RenderTickCounter.ZERO);
    }
}