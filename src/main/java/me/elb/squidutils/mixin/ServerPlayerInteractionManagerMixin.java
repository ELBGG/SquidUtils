package me. elb.squidutils.mixin;

import me.elb.squidutils.server.playereliminated.SoulDepartureSystem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered. asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm. mixin.injection.Inject;
import org.spongepowered.asm.mixin. injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager. class)
public class ServerPlayerInteractionManagerMixin {

    @Final
    @Shadow
    protected ServerPlayerEntity player;

    /**
     * Previene cambios de gamemode externos durante el estado de alma
     */
    @Inject(method = "changeGameMode", at = @At("HEAD"), cancellable = true)
    private void preventGameModeChange(GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
        if (SoulDepartureSystem. isSoulState(player.getUuid())) {
            // Solo permitir si viene del sistema interno
            if (gameMode != GameMode.SPECTATOR) {
                cir.setReturnValue(false);
            }
        }
    }
}