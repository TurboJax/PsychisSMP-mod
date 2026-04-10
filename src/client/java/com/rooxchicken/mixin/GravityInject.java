package com.rooxchicken.mixin;

import com.rooxchicken.client.PsychisModClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class GravityInject {
    PlayerEntity player;
    @ModifyVariable(method = "travelMidAir(Lnet/minecraft/util/math/Vec3d;)V", at = @At("STORE"), ordinal = 0)
    private double travelInject(double d) {
        return PsychisModClient.playerAbility == 5 ? 0.1D : d;
    }
}
