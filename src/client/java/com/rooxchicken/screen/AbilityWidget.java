package com.rooxchicken.screen;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class AbilityWidget extends ClickableWidget {
    public static final Logger logger = LoggerFactory.getLogger("physics-mod");
    public final int index;
    public final int baseWidth = 25;
    public final int baseHeight = 25;
    public final int minWidth = 10;

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

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        // Getting the position
        int x1 = getX();
        int y1 = getY();
        int x2 = x1 + width;
        int y2 = y1 + height;

        // Drawing the scale manipulation box
        context.fill(x2 - 8, y2 - 8, x2, y2, scaling ? -65308 : -1);

        // Drawing the border
        context.drawStrokedRectangle(0, 0, width, height, clicked & !scaling ? -65308 : -1);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        appendDefaultNarrations(builder);
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        clicked = click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT;
        if (clicked) {
            scaling = AABBCheck(click.x(), click.y(), getX() + width - 8, getX() + width, getY() + height - 8, getY() + height);

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

            int newWidth = Math.max(tmpWidth + Math.min(xDiff, yDiff), minWidth);
            setDimensions(newWidth, newWidth);
        } else if (clicked) {
            setPosition((int) (click.x() - mouseRelX), (int) (click.y() - mouseRelY));
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    public void reset() {
        setPosition(30 * index + 10, 20);
        setDimensions(baseWidth, baseHeight);
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
        setWidth(Integer.parseInt(input.nextLine()));
    }
}
