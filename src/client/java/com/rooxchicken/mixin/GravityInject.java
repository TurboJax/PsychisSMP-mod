package com.rooxchicken.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.rooxchicken.client.PsychisModClient;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class GravityInject {
    @ModifyReturnValue(method = "getGravity()D", at = @At("RETURN"))
    private double getGravityModified(double d) {
        return PsychisModClient.playerAbility == 5 ? 0.1D : d;
    }
}