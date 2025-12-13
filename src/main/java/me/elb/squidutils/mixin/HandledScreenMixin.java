package me.elb. squidutils. mixin;

import me.elb.squidutils.client. data.ClientLimitedInventory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft. client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered. asm.mixin. Mixin;
import org. spongepowered.asm.mixin.Shadow;
import org. spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin. injection.Inject;
import org.spongepowered. asm.mixin.injection.callback.CallbackInfo;

import org.jetbrains.annotations.Nullable;

@Mixin(HandledScreen. class)
public abstract class HandledScreenMixin {

    @Shadow
    protected int x;

    @Shadow
    protected int y;

    @Shadow
    @Nullable
    protected Slot focusedSlot;

    @Shadow
    protected abstract Slot getSlotAt(double x, double y);

    /**
     * Oculta slots no permitidos visualmente
     */
    @Inject(method = "drawSlot", at = @At("HEAD"), cancellable = true)
    private void hideDisallowedSlots(DrawContext context, Slot slot, CallbackInfo ci) {
        // Solo aplicar en la pantalla de inventario del jugador
        if (! ((Object) this instanceof InventoryScreen)) {
            return;
        }

        if (!ClientLimitedInventory.isEnabled()) {
            return;
        }

        // Verificar si el slot debe estar oculto
        int index = slot.getIndex();

        boolean isHotbar = index >= 0 && index <= 8;
        boolean isArmor = index >= 36 && index <= 39;
        boolean isOffhand = index == 40;

        if (!isHotbar && !isArmor && !isOffhand) {
            // No dibujar el slot
            ci.cancel();
        }
    }

    /**
     * Previene hover en slots ocultos
     */
    @Inject(method = "drawMouseoverTooltip", at = @At("HEAD"), cancellable = true)
    private void preventTooltipOnHiddenSlots(DrawContext context, int x, int y, CallbackInfo ci) {
        if (!((Object) this instanceof InventoryScreen)) {
            return;
        }

        if (!ClientLimitedInventory.isEnabled()) {
            return;
        }

        // Usar el slot enfocado (focusedSlot) en lugar de getSlotAt
        if (this.focusedSlot != null) {
            int index = this.focusedSlot.getIndex();

            boolean isHotbar = index >= 0 && index <= 8;
            boolean isArmor = index >= 36 && index <= 39;
            boolean isOffhand = index == 40;

            if (! isHotbar && !isArmor && !isOffhand) {
                ci.cancel();
            }
        }
    }
}