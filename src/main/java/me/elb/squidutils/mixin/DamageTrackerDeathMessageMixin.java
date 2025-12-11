package me.elb.squidutils.mixin;

import me.  elb.squidutils. server. playereliminated.CustomDeathMessageSystem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered. asm.  mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered. asm.  mixin.injection.Inject;
import org.spongepowered.asm.mixin. injection.callback.CallbackInfoReturnable;

@Mixin(DamageTracker.class)
abstract class DamageTrackerDeathMessageMixin {

    @Final
    @Shadow
    private LivingEntity entity;

    @Inject(method = "getDeathMessage", at = @At("RETURN"), cancellable = true)
    private void injectCustomDeathMessage(CallbackInfoReturnable<Text> cir) {
        if (!CustomDeathMessageSystem.isActive()) return;

        if (this.entity instanceof ServerPlayerEntity player) {
            Text customMessage = CustomDeathMessageSystem.buildDeathMessage(player);
            cir.setReturnValue(customMessage);
        }
    }
}