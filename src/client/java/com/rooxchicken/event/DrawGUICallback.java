package com.rooxchicken.event;

import com.rooxchicken.client.PsychisModClient;
import com.rooxchicken.data.AbilityData;
import com.rooxchicken.data.HandleData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2fStack;

public class DrawGUICallback implements HudRenderCallback {
    private Matrix3x2fStack matrixStack;

    public void onHudRender(@NotNull DrawContext drawContext, @NotNull RenderTickCounter tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        if (PsychisModClient.playerAbility != -2) {
            AbilityData abilityData = PsychisModClient.abilityData;
            float scale1 = PsychisModClient.abilityElement1.getWidth() / 64f;
            float pos1X = PsychisModClient.abilityElement1.getX() * (1.0f / scale1);
            float pos1Y = PsychisModClient.abilityElement1.getY() * (1.0f / scale1);
            float scale2 = PsychisModClient.abilityElement2.getWidth() / 64f;
            float pos2X = PsychisModClient.abilityElement2.getX() * (1.0f / scale2);
            float pos2Y = PsychisModClient.abilityElement2.getY() * (1.0f / scale2);
            int cooldown1 = abilityData.cooldowns.get(0);
            int cooldown2 = abilityData.cooldowns.get(1);
            int cooldown1Max = abilityData.cooldownMaxes.get(0);
            int cooldown2Max = abilityData.cooldownMaxes.get(1);
            String txt1 = "";
            String txt2 = "";
            if (cooldown1 > 0) {
                txt1 = txt1 + cooldown1;
            } else if (cooldown1 == -1) {
                txt1 = "0";
            } else {
                txt1 = "READY";
            }

            if (cooldown2 > 0) {
                txt2 = txt2 + cooldown2;
            } else if (cooldown2 == -1) {
                txt2 = "0";
            } else {
                txt2 = "READY";
            }

            this.startScaling(drawContext, scale1);
            this.matrixStack.translate(pos1X, pos1Y);
            drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, abilityData.textures.get(0), 0, 0, 0, 0, 64, 64, 64, 64);
            int cooldown1Offset = (int) (64.0D * ((0.0D + (double) cooldown1) / (double) cooldown1Max));
            drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, abilityData.cooldownTexture, 0, 64 - cooldown1Offset, 0, 0, 64, cooldown1Offset, 64, 64, 0x7FFFFFFF);
            drawContext.drawStrokedRectangle(0, 0, 64, 64, 0x7FFFFFFF);
            this.stopScaling(drawContext);
            this.startScaling(drawContext, scale2);
            this.matrixStack.translate(pos2X, pos2Y);
            drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, HandleData.dragonEggAbilityOverride == -1 ? abilityData.textures.get(1) : Identifier.of("psychis-mod", "textures/abilities/" + HandleData.dragonEggAbilityOverride + "_1.png"), 0, 0, 0, 0, 64, 64, 64, 64);
            int cooldown2Offset = (int) (64.0D * ((0.0D + (double) cooldown2) / (double) cooldown2Max));
            drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, abilityData.cooldownTexture, 0, 64 - cooldown2Offset, 0, 0, 64, cooldown2Offset, 64, 64, 0x7FFFFFFF);
            if (abilityData.secondLocked) {
                drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("psychis-mod", "textures/gui/locked.png"), 0, 0, 0, 0, 64, 64, 64, 64, 0xFFFFFFFF);
                txt2 = "LOCKED";
            }

            drawContext.drawStrokedRectangle(0, 0, 64, 64, 0x7FFFFFFF);
            this.stopScaling(drawContext);
            this.startScaling(drawContext, scale1 * 1.5F);
            this.matrixStack.translate((float) (pos1X / 1.5), (float) (pos1Y / 1.5));
            drawContext.drawCenteredTextWithShadow(textRenderer, txt1, 23, -10, -1);
            this.stopScaling(drawContext);
            this.startScaling(drawContext, scale2 * 1.5F);
            this.matrixStack.translate((float) (pos2X / 1.5), (float) (pos2Y / 1.5));
            drawContext.drawCenteredTextWithShadow(textRenderer, txt2, 23, -10, -1);
            this.stopScaling(drawContext);
        }
    }

    private void startScaling(DrawContext drawContext, float scale) {
        this.matrixStack = drawContext.getMatrices();
        this.matrixStack.pushMatrix();
        this.matrixStack.scale(scale, scale);
    }

    private void stopScaling(DrawContext drawContext) {
        this.matrixStack.popMatrix();
    }
}
