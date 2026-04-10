package com.rooxchicken.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import com.rooxchicken.client.PsychisModClient;
import com.rooxchicken.data.AbilityDesc;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.IconWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;


public class AbilitySelection extends Screen {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private Identifier bgTexture;
    private int index = 0;
    private int clickAction = -1;

    public AbilitySelection(Text title) {
        super(title);

        AbilityDesc ability = PsychisModClient.abilities.get(this.index);

        IconWidget img1 = IconWidget.create(24, 24, Identifier.of("psychis-mod", "textures/gui/left.png"), 24, 24);
        img1.setTooltip(Tooltip.of(Text.literal("Second ability unlock: " + ability.secondUnlock)));
        addDrawable(img1);
    }

    public void init() {
        this.bgTexture = Identifier.of("psychis-mod", "textures/gui/bg.png");
    }

    @Override
    public boolean mouseClicked(Click mouseButtonEvent, boolean doubled) {
        if (this.clickAction != -1) {
            this.client.world.playSound(this.client.player, this.client.player.getBlockPos(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.AMBIENT, 0.6F, 1.0F);
            switch (this.clickAction) {
                case 0:
                    if (--this.index < 0) {
                        this.index = PsychisModClient.abilities.size() - 1;
                    }
                    break;
                case 1:
                    if (++this.index > PsychisModClient.abilities.size() - 1) {
                        this.index = 0;
                    }
                    break;
                case 2:
                    PsychisModClient.sendChatCommand("hdn_pickability " + PsychisModClient.abilities.get(this.index).index);
                    this.close();
            }
        }

        return super.mouseClicked(mouseButtonEvent, doubled);
    }

    @Override
    public boolean keyPressed(KeyInput keyEvent) {
        if (keyEvent.key() == GLFW.GLFW_KEY_ESCAPE) {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("You may reopen this menu at any time by running the command /selectability. Until then, you will remain ability-less."), false);
            this.close();
            return true;
        }

        return super.keyPressed(keyEvent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();

        super.render(context, mouseX, mouseY, delta);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, this.bgTexture, this.width / 2 - 128, this.height / 2 - 128, 0, 0, 256, 256, 256, 256);
        AbilityDesc ability = PsychisModClient.abilities.get(this.index);
        int yIndex = 0;
        context.drawText(this.textRenderer, Text.literal(ability.name), this.width / 2, this.height / 2 - 90, -1, false);
        String[] passives = ability.passive.split("\\+");

        for (int i = 0; i < passives.length; ++i) {
            context.drawText(this.textRenderer, Text.literal(passives[i]), this.width / 2 + 6, this.height / 2 - 78 + 12 * i, -6710887, false);
        }

        context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("psychis-mod", "textures/abilities/" + ability.index + ".png"), this.width / 2 - 90, this.height / 2 - 90, 0, 0, 24, 24, 24, 24);
        context.drawText(this.textRenderer, Text.literal(ability.ability1Name), this.width / 2 - 90, this.height / 2 - 52, -48060, false);
        String[] ability1Desc = ability.ability1Desc.split("\\+");

        for (int i = 0; i < ability1Desc.length; ++i) {
            context.drawText(this.textRenderer, Text.literal(ability1Desc[i]), this.width / 2 - 90, this.height / 2 - 40 + 12 * i, -3355444, false);
            ++yIndex;
        }

        context.drawText(this.textRenderer, Text.literal(ability.ability2Name), this.width / 2 - 90, this.height / 2 - 40 + 12 * (yIndex + 1), -48060, false);
        String[] ability2Desc = ability.ability2Desc.split("\\+");

        for (String s : ability2Desc) {
            context.drawText(this.textRenderer, Text.literal(s), this.width / 2 - 90, this.height / 2 - 40 + 12 * (yIndex + 2), -3355444, false);
            ++yIndex;
        }

        context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("psychis-mod", "textures/gui/left.png"), this.width / 2 - 90, this.height / 2 + 70, 0, 0, 24, 24, 24, 24);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("psychis-mod", "textures/gui/right.png"), this.width / 2 + 60, this.height / 2 + 70, 0, 0, 24, 24, 24, 24);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("psychis-mod", "textures/gui/unlock.png"), this.width / 2 + 30, this.height / 2 + 70, 0, 0, 24, 24, 24, 24);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("psychis-mod", "textures/gui/select.png"), this.width / 2 - 54, this.height / 2 + 70, 0, 0, 72, 24, 72, 24);
        double scalingFactor = client.getWindow().getScaleFactor();
        double screenX = (double) mouseX * scalingFactor;
        double screenY = (double) mouseY * scalingFactor;
        this.clickAction = -1;
        if (screenX > (double) (this.width / 2 - 90) * scalingFactor && screenX < (double) (this.width / 2 - 90) * scalingFactor + 24.0D * scalingFactor && screenY > (double) (this.height / 2 + 70) * scalingFactor && screenY < (double) (this.height / 2 + 70) * scalingFactor + 24.0D * scalingFactor) {
            context.drawTooltip(Text.literal("Back"), mouseX, mouseY);
            this.clickAction = 0;
        }

        if (screenX > (double) (this.width / 2 + 60) * scalingFactor && screenX < (double) (this.width / 2 + 60) * scalingFactor + 24.0D * scalingFactor && screenY > (double) (this.height / 2 + 70) * scalingFactor && screenY < (double) (this.height / 2 + 70) * scalingFactor + 24.0D * scalingFactor) {
            context.drawTooltip(Text.literal("Next"), mouseX, mouseY);
            this.clickAction = 1;
        }

        if (screenX > (double) (this.width / 2 + 30) * scalingFactor && screenX < (double) (this.width / 2 + 30) * scalingFactor + 24.0D * scalingFactor && screenY > (double) (this.height / 2 + 70) * scalingFactor && screenY < (double) (this.height / 2 + 70) * scalingFactor + 24.0D * scalingFactor) {
            context.drawTooltip(Text.literal("Second ability unlock: " + ability.secondUnlock), mouseX, mouseY);
            this.clickAction = -1;
        }

        if (screenX > (double) (this.width / 2 - 54) * scalingFactor && screenX < (double) (this.width / 2 - 54) * scalingFactor + 72.0D * scalingFactor && screenY > (double) (this.height / 2 + 70) * scalingFactor && screenY < (double) (this.height / 2 + 70) * scalingFactor + 24.0D * scalingFactor) {
            context.drawTooltip(Text.literal("Select this ability (choose wisely!)"), mouseX, mouseY);
            this.clickAction = 2;
        }
    }
}
