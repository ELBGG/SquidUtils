package me.elb.squidutils.mixin;

import me. elb.squidutils.server.limitedinventory.LimitedInventorySystem;
import net.minecraft.entity.player. PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org. spongepowered. asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org. spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin. injection.Inject;
import org.spongepowered. asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered. asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Final
    @Shadow
    public PlayerEntity player;

    /**
     * Bloquea insertar items en slots no permitidos
     */
    @Inject(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void blockInsertIntoDisallowedSlots(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!LimitedInventorySystem.isPlayerLimited(player.getUuid())) {
            return;
        }

        boolean isHotbar = slot >= 0 && slot <= 8;
        boolean isArmor = slot >= 36 && slot <= 39;
        boolean isOffhand = slot == 40;

        if (!isHotbar && !isArmor && !isOffhand) {
            cir.setReturnValue(false);
        }
    }

    /**
     * Bloquea setear stacks en slots no permitidos
     */
    @Inject(method = "setStack", at = @At("HEAD"), cancellable = true)
    private void blockSetStackInDisallowedSlots(int slot, ItemStack stack, CallbackInfo ci) {
        if (!LimitedInventorySystem.isPlayerLimited(player.getUuid())) {
            return;
        }

        boolean isHotbar = slot >= 0 && slot <= 8;
        boolean isArmor = slot >= 36 && slot <= 39;
        boolean isOffhand = slot == 40;

        if (!isHotbar && !isArmor && ! isOffhand && ! stack.isEmpty()) {
            // Cancelar la operación (no se puede setear en este slot)
            ci.cancel();
        }
    }

    /**
     * Bloquea obtener items de slots no permitidos
     */
    @Inject(method = "removeStack(I)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private void blockRemoveFromDisallowedSlots(int slot, CallbackInfoReturnable<ItemStack> cir) {
        if (!LimitedInventorySystem.isPlayerLimited(player.getUuid())) {
            return;
        }

        boolean isHotbar = slot >= 0 && slot <= 8;
        boolean isArmor = slot >= 36 && slot <= 39;
        boolean isOffhand = slot == 40;

        if (!isHotbar && ! isArmor && !isOffhand) {
            cir.setReturnValue(ItemStack. EMPTY);
        }
    }
}