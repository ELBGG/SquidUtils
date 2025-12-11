package me.elb. squidutils.mixin;

import me.  elb.squidutils. server. playereliminated.CustomDeathMessageSystem;
import net. minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered. asm.  mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered. asm.  mixin.injection.Inject;
import org.spongepowered.asm.mixin. injection.callback.CallbackInfo;

/**
 * Mixin para interceptar la muerte del jugador
 */
@Mixin(ServerPlayerEntity.class)
public class PlayerEntityDeathMixin {

    /**
     * Intercepta la muerte ANTES de que se procese
     */
    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        
        if (CustomDeathMessageSystem.isActive()) {
            CustomDeathMessageSystem.onPlayerDeath(player);
            ci.cancel();
        }
    }
}

