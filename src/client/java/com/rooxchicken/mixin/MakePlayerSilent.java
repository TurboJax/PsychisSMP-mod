package com.rooxchicken.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.rooxchicken.data.HandleData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class MakePlayerSilent {
    @Shadow
    public abstract String getStringifiedName();

    @ModifyReturnValue(method = "getMoveEffect", at = @At("RETURN"))
    public Entity.MoveEffect movementEmissionMixin(Entity.MoveEffect retval) {
        return HandleData.silentPlayers.contains(getStringifiedName()) ? Entity.MoveEffect.NONE : retval;
    }
}
