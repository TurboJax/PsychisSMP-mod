package com.rooxchicken.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.Scanner;

public class AbilityElement {
    public float smallestSize = 0.2F;
    public float snapIncrement = 4;
    public int index;
    public int mouseStatus = -2;
    public int manipulationStatus = -1;
    public int x = -1;
    public int y = -1;
    public float scale = -1;
    protected final int width = 64;
    protected final int height = 64;
    protected int oldMouseX = 0;
    protected int oldMouseY = 0;
    protected int oldX = 0;
    protected int oldY = 0;
    protected float oldScale = 0;
    protected int length;

    public AbilityElement(int index) {
        this.index = index;
        this.reset();
    }

    public void HandleLines(ConfigScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY) {
        int x1 = this.x;
        int x2 = (int) (this.x + this.width * this.scale);
        int y1 = this.y;
        int y2 = (int) (this.y + this.height * this.scale);
        if (this.manipulationStatus == 2 && this.mouseStatus > -1) {
            context.fill(x1, y1, x1 + 8, y1 + 8, -65308);
        } else {
            context.fill(x1, y1, x1 + 8, y1 + 8, -1);
        }

        context.drawHorizontalLine(x1, x2, y1, -1);
        context.drawHorizontalLine(x1, x2, y2, -1);
        context.drawVerticalLine(x1, y1, y2, -1);
        context.drawVerticalLine(x2, y1, y2, -1);
        if (!screen.ObjectSelected && this.mouseStatus > -1 && this.manipulationStatus == -1) {
            if (this.AABBCheck(mouseX, mouseY, x1, x2, y1, y2)) {
                this.manipulationStatus = 1;
                if (this.AABBCheck(mouseX, mouseY, x1, x1 + 8, y1, y1 + 8)) {
                    this.manipulationStatus = 2;
                    this.oldScale = this.scale;
                    this.length = x2 - x1;
                    this.oldMouseX = mouseX;
                    this.oldMouseY = mouseY;
                    this.oldX = this.x;
                    this.oldY = this.y;
                }

                screen.ObjectSelected = true;
            } else {
                this.manipulationStatus = 0;
            }
        } else if (this.mouseStatus == -1) {
            this.manipulationStatus = -1;
            screen.ObjectSelected = false;
        }

        if (this.manipulationStatus == 1) {
            this.x += mouseX - this.oldMouseX;
            this.y += mouseY - this.oldMouseY;
        }

        if (this.manipulationStatus == 2) {
            this.HandleScaling(mouseX, mouseY);
            if (this.scale < this.smallestSize) {
                this.scale = this.smallestSize;
            }
        } else {
            this.oldMouseX = mouseX;
            this.oldMouseY = mouseY;
        }
    }

    public void HandleScaling(int mouseX, int mouseY) {
        float mScale = Math.max(mouseX - this.oldMouseX, mouseY - this.oldMouseY);
        this.scale = this.oldScale * (this.length - mScale) / this.length;
        if (this.scale < this.smallestSize) {
            this.scale = this.smallestSize;
        } else {
            if (this.mouseStatus == 1) {
                this.scale = this.scale * this.snapIncrement / this.snapIncrement;
            }

            this.x = (int) (this.oldX + (this.oldScale - this.scale) * this.width);
            this.y = (int) (this.oldY + (this.oldScale - this.scale) * this.height);
        }
    }

    protected boolean AABBCheck(int mouseX, int mouseY, int x1, int x2, int y1, int y2) {
        return mouseX > x1 && mouseX < x2 && mouseY < y2 && mouseY > y1;
    }

    public void reset() {
        this.x = 30 * this.index + 10;
        this.y = 20;
        this.scale = 0.4F;
        this.smallestSize = 0.15F;
    }

    public String save() {
        return this.x + "\n" + this.y + "\n" + this.scale + "\n";
    }

    public void load(Scanner input) {
        this.x = Integer.parseInt(input.nextLine());
        this.y = Integer.parseInt(input.nextLine());
        this.scale = Float.parseFloat(input.nextLine());
    }
}
