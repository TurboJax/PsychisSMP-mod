package com.rooxchicken.screen;

import com.rooxchicken.client.PsychisModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class CardSelect extends Screen {
    private final Identifier bgTexture;
    private int clickAction = -1;
    private Identifier blankCard;
    private Identifier card1;
    private Identifier card2;
    private Identifier card3;
    private double s1 = 1.0D;
    private double s2 = 1.0D;
    private double s3 = 1.0D;
    private boolean clickLocked = false;
    private int state = 0;
    private double t = 0.0D;
    private final int[] cards;
    private final String[] cardDesc;

    public CardSelect(Text title, int[] _cards, String[] _cardDesc) {
        super(title);
        this.cards = _cards;
        this.cardDesc = _cardDesc;
        this.bgTexture = Identifier.of("psychis-mod", "textures/gui/bg_card.png");
        this.blankCard = Identifier.of("psychis-mod", "textures/cards/blank_0.png");
        this.setCards();
    }

    private void setCards() {
        this.card1 = this.blankCard;
        this.card2 = this.blankCard;
        this.card3 = this.blankCard;
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (this.clickAction != -1 && this.state == 0) {
            this.client.world.playSound(this.client.player, this.client.player.getBlockPos(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.AMBIENT, 0.6F, 1.0F);
            this.clickLocked = true;
            this.state = 1;
        }

        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(Click click) {
        return super.mouseReleased(click);
    }

    @Override
    public void tick() {
        if (this.state == 1) {
            if ((int) this.t < 21) {
                this.blankCard = Identifier.of("psychis-mod", "textures/cards/blank_" + (int) this.t + ".png");
                this.setCards();
            }

            if ((int) this.t > 20) {
                this.card1 = Identifier.of("psychis-mod", "textures/cards/card_" + this.cards[0] + ".png");
                this.card2 = Identifier.of("psychis-mod", "textures/cards/card_" + this.cards[1] + ".png");
                this.card3 = Identifier.of("psychis-mod", "textures/cards/card_" + this.cards[2] + ".png");
                this.state = 2;
                PsychisModClient.sendChatCommand("pickcard " + this.cards[this.clickAction]);
            }
        }

    }

    @Override
    public boolean keyPressed(KeyInput keyInput) {
        if (keyInput.key() == GLFW.GLFW_KEY_ESCAPE) {
            if (this.state != 2) {
                MinecraftClient.getInstance().player.sendMessage(Text.of("You may reopen this menu at any time by running the command /selectcards. Until then, you will remain cardless."), false);
            }

            close();
            return true;
        }

        return super.keyPressed(keyInput);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        super.render(context, mouseX, mouseY, delta);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, this.bgTexture, this.width / 2 - 128, this.height / 2 - 128, 0, 0, 256, 256, 256, 256);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, this.card1, this.width / 2 - 80 - (int) (26.0D * this.s1), this.height / 2 - (int) (18.0D * this.s1 * 2.0D), 0, 0, (int) (52.0D * this.s1), (int) (62.0D * this.s1), (int) (52.0D * this.s1), (int) (62.0D * this.s1));
        context.drawTexture(RenderPipelines.GUI_TEXTURED, this.card2, this.width / 2 - (int) (26.0D * this.s2), this.height / 2 - (int) (18.0D * this.s2 * 2.0D), 0, 0, (int) (52.0D * this.s2), (int) (62.0D * this.s2), (int) (52.0D * this.s2), (int) (62.0D * this.s2));
        context.drawTexture(RenderPipelines.GUI_TEXTURED, this.card3, this.width / 2 + 80 - (int) (26.0D * this.s3), this.height / 2 - (int) (18.0D * this.s3 * 2.0D), 0, 0, (int) (52.0D * this.s3), (int) (62.0D * this.s3), (int) (52.0D * this.s3), (int) (62.0D * this.s3));
        double scalingFactor = client.getWindow().getScaleFactor();
        double screenX = (double) mouseX * scalingFactor;
        double screenY = (double) mouseY * scalingFactor;
        if (this.state == 1) {
            this.t += delta;
        }

        if (!this.clickLocked) {
            this.clickAction = -1;
        }

        if (this.state == 0) {
            if (screenX > (double) (this.width / 2 - 80 - (int) (26.0D * this.s1)) * scalingFactor && screenX < (double) (this.width / 2 - 80 - (int) (26.0D * this.s1)) * scalingFactor + 52.0D * this.s1 * scalingFactor && screenY > (double) (this.height / 2 - (int) (18.0D * this.s1 * 2.0D)) * scalingFactor && screenY < (double) (this.height / 2 - (int) (18.0D * this.s1 * 2.0D)) * scalingFactor + 62.0D * this.s1 * scalingFactor) {
                context.drawTooltip(Text.literal("Card #1"), mouseX, mouseY);
                this.clickAction = 0;
            }

            if (screenX > (double) (this.width / 2 - (int) (26.0D * this.s2)) * scalingFactor && screenX < (double) (this.width / 2 - (int) (26.0D * this.s2)) * scalingFactor + 52.0D * this.s2 * scalingFactor && screenY > (double) (this.height / 2 - (int) (18.0D * this.s2 * 2.0D)) * scalingFactor && screenY < (double) (this.height / 2 - (int) (18.0D * this.s2 * 2.0D)) * scalingFactor + 62.0D * this.s2 * scalingFactor) {
                context.drawTooltip(Text.literal("Card #2"), mouseX, mouseY);
                this.clickAction = 1;
            }

            if (screenX > (double) (this.width / 2 + 80 - (int) (26.0D * this.s3)) * scalingFactor && screenX < (double) (this.width / 2 + 80 - (int) (26.0D * this.s3)) * scalingFactor + 52.0D * this.s3 * scalingFactor && screenY > (double) (this.height / 2 - (int) (18.0D * this.s3 * 2.0D)) * scalingFactor && screenY < (double) (this.height / 2 - (int) (18.0D * this.s3 * 2.0D)) * scalingFactor + 62.0D * this.s3 * scalingFactor) {
                context.drawTooltip(Text.literal("Card #3"), mouseX, mouseY);
                this.clickAction = 2;
            }
        } else if (this.state == 2) {
            if (screenX > (double) (this.width / 2 - 80 - (int) (26.0D * this.s1)) * scalingFactor && screenX < (double) (this.width / 2 - 80 - (int) (26.0D * this.s1)) * scalingFactor + 52.0D * this.s1 * scalingFactor && screenY > (double) (this.height / 2 - (int) (18.0D * this.s1 * 2.0D)) * scalingFactor && screenY < (double) (this.height / 2 - (int) (18.0D * this.s1 * 2.0D)) * scalingFactor + 62.0D * this.s1 * scalingFactor) {
                context.drawTooltip(Text.literal(this.cardDesc[0]), mouseX, mouseY);
            }

            if (screenX > (double) (this.width / 2 - (int) (26.0D * this.s2)) * scalingFactor && screenX < (double) (this.width / 2 - (int) (26.0D * this.s2)) * scalingFactor + 52.0D * this.s2 * scalingFactor && screenY > (double) (this.height / 2 - (int) (18.0D * this.s2 * 2.0D)) * scalingFactor && screenY < (double) (this.height / 2 - (int) (18.0D * this.s2 * 2.0D)) * scalingFactor + 62.0D * this.s2 * scalingFactor) {
                context.drawTooltip(Text.literal(this.cardDesc[1]), mouseX, mouseY);
            }

            if (screenX > (double) (this.width / 2 + 80 - (int) (26.0D * this.s3)) * scalingFactor && screenX < (double) (this.width / 2 + 80 - (int) (26.0D * this.s3)) * scalingFactor + 52.0D * this.s3 * scalingFactor && screenY > (double) (this.height / 2 - (int) (18.0D * this.s3 * 2.0D)) * scalingFactor && screenY < (double) (this.height / 2 - (int) (18.0D * this.s3 * 2.0D)) * scalingFactor + 62.0D * this.s3 * scalingFactor) {
                context.drawTooltip(Text.literal(this.cardDesc[2]), mouseX, mouseY);
            }
        }

        double v = Math.max(0.05D, Math.min(0.95D, (double) delta));
        if (this.clickAction == 0) {
            this.s1 = this.lerp(this.s1, 1.5D, v);
        } else {
            this.s1 = this.lerp(this.s1, 1.0D, v);
        }

        if (this.clickAction == 1) {
            this.s2 = this.lerp(this.s2, 1.5D, v);
        } else {
            this.s2 = this.lerp(this.s2, 1.0D, v);
        }

        if (this.clickAction == 2) {
            this.s3 = this.lerp(this.s3, 1.5D, v);
        } else {
            this.s3 = this.lerp(this.s3, 1.0D, v);
        }

    }

    private double lerp(double a, double b, double f) {
        return a * (1.0D - f) + b * f;
    }
}
