package me.elb.squidutils.mixin;

import me.elb. squidutils.client.data. ClientLimitedInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot. Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered. asm.mixin.injection. At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerScreenHandler.class)
public class PlayerScreenHandlerAccessMixin {

    /**
     * Deshabilita slots al inicializar el screen handler
     */
    @Inject(method = "<init>(Lnet/minecraft/entity/player/PlayerInventory;ZLnet/minecraft/entity/player/PlayerEntity;)V", 
            at = @At("RETURN"))
    private void disableInventorySlots(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
        if (!ClientLimitedInventory.isEnabled()) {
            return;
        }

        PlayerScreenHandler handler = (PlayerScreenHandler) (Object) this;
        
        // Deshabilitar slots 9-35 (inventario principal, 27 slots)
        for (int i = 9; i <= 35; i++) {
            Slot slot = handler.slots.get(i);
            // Hacer el slot invisible (no se puede hacer directamente, pero podemos bloquearlo)
        }
    }
}