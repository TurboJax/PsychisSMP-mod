package com.rooxchicken.screen;

import com.rooxchicken.client.PsychisModClient;
import com.rooxchicken.data.AbilityData;
import com.rooxchicken.data.HandleData;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2fStack;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class AbilityWidget extends ClickableWidget implements HudElement {
    public static final Logger logger = LoggerFactory.getLogger("physics-mod");
    public final int index;

    public float scale = 1.06f;
    public final float minScale = 0.4f;

    public boolean clicked = false;
    public boolean scaling = false;
    public double mouseRelX = 0;
    public double mouseRelY = 0;
    public int tmpWidth;

    public AbilityWidget(int index) {
        super(0, 0, 0, 0, Text.of(""));

        this.index = index;
        reset();
    }

    private void render(DrawContext context) {
        // Updating the dimensions
        width = (int) (scale * 24);
        height = (int) (scale * 24);

        // Getting the ability data
        AbilityData abilityData = PsychisModClient.abilityData;
        int cooldown = abilityData.cooldowns.get(index);
        int cooldownMax = abilityData.cooldownMaxes.get(index);

        // Prepping the scalar
        Matrix3x2fStack matrixStack = startScaling(context, scale);
        matrixStack.translate(getX() / scale, getY() / scale);

        // Drawing the glyphs
        Identifier texture = abilityData.textures.get(index);
        if (index == 1 && HandleData.dragonEggAbilityOverride != -1) texture = Identifier.of("psychis-mod", "textures/abilities/" + HandleData.dragonEggAbilityOverride + "_1.png");
        context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, 0, 0, 0, 0, 24, 24, 24, 24);
        int cooldownOffset = 24 * cooldown / cooldownMax;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, abilityData.cooldownTexture, 0, 24 - cooldownOffset, 0, 0, 24, cooldownOffset, 24, 24, 0x7FFFFFFF);
        if (index == 1 && abilityData.secondLocked) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("psychis-mod", "textures/gui/locked.png"), 0, 0, 0, 0, 24, 24, 24, 24, 0xFFFFFFFF);
        }

        // Drawing the config boxes in different ways
        if (MinecraftClient.getInstance().currentScreen instanceof ConfigScreen) {
            // Rendering position rectangle above the scale manipulation box
            if (clicked & !scaling) {
                context.fill(18, 18, 24, 24, -1);
                context.drawStrokedRectangle(0, 0, 24, 24, -65308);
            } else if (scaling) {
                context.drawStrokedRectangle(0, 0, 24, 24, -1);
                context.fill(18, 18, 24, 24, -65308);
            } else {
                context.fill(18, 18, 24, 24, -1);
                context.drawStrokedRectangle(0, 0, 24, 24, -1);
            }
        }

        // Rendering the scaled object
        matrixStack.popMatrix();

        // Determining the status
        String txt = "READY";
        if (cooldown > 0) {
            txt = String.valueOf(cooldown);
        } else if (cooldown == -1) {
            txt = "0";
        }

        if (index == 1 && abilityData.secondLocked) {
            txt = "LOCKED";
        }

        // Writing the status
        matrixStack = startScaling(context, scale / 2);
        matrixStack.translate(getX() / scale * 2, getY() / scale * 2);
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, txt, 24, -10, -1);
        matrixStack.popMatrix();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        render(context);
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        render(context);
    }

    private Matrix3x2fStack startScaling(DrawContext drawContext, float scale) {
        Matrix3x2fStack matrixStack = drawContext.getMatrices();
        matrixStack.pushMatrix();
        matrixStack.scale(scale, scale);
        return matrixStack;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        appendDefaultNarrations(builder);
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        clicked = click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT;
        if (clicked) {
            scaling = AABBCheck(click.x(), click.y(), getX() + width * 0.75, getX() + width, getY() + height * 0.75, getY() + height);

            mouseRelX = click.x() - getX();
            mouseRelY = click.y() - getY();
            if (scaling) {
                tmpWidth = getWidth();
            }
        }

        super.onClick(click, doubled);
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            clicked = false;
            scaling = false;
        }

        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (scaling) {
            int x1 = getX();
            int x2 = (int) (click.x() - mouseRelX);
            int xDiff = x2 - x1;

            int y1 = getY();
            int y2 = (int) (click.y() - mouseRelY);
            int yDiff = y2 - y1;

            scale = Math.max((tmpWidth + Math.min(xDiff, yDiff)) / 24f, minScale);
        } else if (clicked) {
            setPosition((int) (click.x() - mouseRelX), (int) (click.y() - mouseRelY));
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    public void reset() {
        setPosition(30 * index + 10, 20);
        scale = 1.06f;
    }

    protected boolean AABBCheck(double mouseX, double mouseY, double x1, double x2, double y1, double y2) {
        return mouseX > x1 && mouseX < x2 && mouseY < y2 && mouseY > y1;
    }

    public String save() {
        return this.getX() + "\n" + this.getY() + "\n" + this.getWidth() + "\n";
    }

    public void load(Scanner input) {
        setX(Integer.parseInt(input.nextLine()));
        setY(Integer.parseInt(input.nextLine()));
        width = Integer.parseInt(input.nextLine());
        height = width;
    }
}
