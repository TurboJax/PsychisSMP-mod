package com.rooxchicken.screen;

import java.util.Scanner;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;

public class AbilityElement {
   public String Name;
   public String Description;
   public boolean Enabled;
   public boolean Visible = true;
   public boolean HasLines = true;
   public float SmallestSize = 0.2F;
   public float SnapIncrement = 4;
   public int index = -1;
   public String KeyName;
   public KeyBinding UsageKey;
   public boolean SettingsOpen;
   public int MouseStatus = -2;
   public int ManipulationStatus = -1;
   public int PositionX = -1;
   public int PositionY = -1;
   public float Scale = -1;
   public double ScaleX;
   public double ScaleY;
   protected int x1Mod = 0;
   protected int x2Mod = 64;
   protected int y1Mod = 0;
   protected int y2Mod = 64;
   protected int oldMouseX = 0;
   protected int oldMouseY = 0;
   protected int oldPositionX = 0;
   protected int oldPositionY = 0;
   protected float oldScale = 0;
   protected int length;

   public AbilityElement(int _index) {
      this.index = _index;
      this.reset();
   }

   public void HandleLines(ConfigScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY) {
      MinecraftClient client = MinecraftClient.getInstance();
      double scalingFactor = client.getWindow().getScaleFactor();
      int x1 = this.PositionX + this.x1Mod;
      int x2 = (int)((double)this.PositionX + (double)this.x2Mod * this.Scale);
      int y1 = this.PositionY + this.y1Mod;
      int y2 = (int)((double)this.PositionY + (double)this.y2Mod * this.Scale);
      if (this.ManipulationStatus == 2 && this.MouseStatus > -1) {
         context.fill(x1, y1, x1 + 8, y1 + 8, -65308);
      } else {
         context.fill(x1, y1, x1 + 8, y1 + 8, -1);
      }

      context.drawHorizontalLine(x1, x2, y1, -1);
      context.drawHorizontalLine(x1, x2, y2, -1);
      context.drawVerticalLine(x1, y1, y2, -1);
      context.drawVerticalLine(x2, y1, y2, -1);
      if (!screen.ObjectSelected && this.MouseStatus > -1 && this.ManipulationStatus == -1) {
         if (this.AABBCheck(mouseX, mouseY, x1, x2, y1, y2)) {
            this.ManipulationStatus = 1;
            if (this.AABBCheck(mouseX, mouseY, x1, x1 + 8, y1, y1 + 8)) {
               this.ManipulationStatus = 2;
               this.oldScale = this.Scale;
               this.length = x2 - x1;
               this.oldMouseX = mouseX;
               this.oldMouseY = mouseY;
               this.oldPositionX = this.PositionX;
               this.oldPositionY = this.PositionY;
            }

            screen.ObjectSelected = true;
         } else {
            this.ManipulationStatus = 0;
         }
      } else if (this.MouseStatus == -1) {
         this.ManipulationStatus = -1;
         screen.ObjectSelected = false;
      }

      if (this.ManipulationStatus == 1) {
         this.PositionX += mouseX - this.oldMouseX;
         this.PositionY += mouseY - this.oldMouseY;
      }

      if (this.ManipulationStatus == 2) {
         this.HandleScaling(mouseX, mouseY);
         if (this.Scale < this.SmallestSize) {
            this.Scale = this.SmallestSize;
         }

      } else {
         this.oldMouseX = mouseX;
         this.oldMouseY = mouseY;
      }
   }

   public void HandleScaling(int mouseX, int mouseY) {
      float mScale = Math.max(mouseX - this.oldMouseX, mouseY - this.oldMouseY);
       this.Scale = this.oldScale * (this.length - mScale) / this.length;
      if (this.Scale < this.SmallestSize) {
         this.Scale = this.SmallestSize;
      } else {
         if (this.MouseStatus == 1) {
            this.Scale = this.Scale * this.SnapIncrement / this.SnapIncrement;
         }

         this.PositionX = (int) (this.oldPositionX + (this.oldScale - this.Scale) * this.x2Mod);
         this.PositionY = (int) (this.oldPositionY + (this.oldScale - this.Scale) * this.y2Mod);
      }
   }

   protected boolean AABBCheck(int mouseX, int mouseY, int x1, int x2, int y1, int y2) {
      return mouseX > x1 && mouseX < x2 && mouseY < y2 && mouseY > y1;
   }

   public void reset() {
      this.PositionX = 30 * this.index + 10;
      this.PositionY = 20;
      this.Scale = 0.4F;
      this.SmallestSize = 0.15F;
   }

   public String save() {
      return this.PositionX + "\n" + this.PositionY + "\n" + this.Scale + "\n";
   }

   public void load(Scanner input) {
      this.PositionX = Integer.parseInt(input.nextLine());
      this.PositionY = Integer.parseInt(input.nextLine());
      this.Scale = Float.parseFloat(input.nextLine());
   }
}
