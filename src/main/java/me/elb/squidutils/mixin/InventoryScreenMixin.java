package me. elb.squidutils.mixin;

import me.elb.squidutils.client.data.ClientLimitedInventory;
import net.minecraft.client. gui.DrawContext;
import net.minecraft.client.gui.screen.ingame. InventoryScreen;
import net. minecraft.entity.player.PlayerInventory;
import net.minecraft. screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered. asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm. mixin.injection.Inject;
import org.spongepowered.asm.mixin. injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    /**
     * Oculta slots del inventario no permitidos
     */
    @Inject(method = "drawBackground", at = @At("RETURN"))
    private void hideInventorySlots(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (!ClientLimitedInventory.isEnabled()) {
            return;
        }

        InventoryScreen screen = (InventoryScreen) (Object) this;

        // Dibujar rectángulos grises sobre los slots bloqueados
        // para ocultarlos visualmente
        int x = (screen.width - 176) / 2;  // Posición X de la GUI
        int y = (screen. height - 166) / 2; // Posición Y de la GUI

        // Ocultar los 27 slots del inventario principal (3 filas de 9)
        // Coordenadas relativas a la textura del inventario
        int slotStartX = x + 8;   // Inicio de los slots en X
        int slotStartY = y + 84;  // Inicio de los slots en Y (debajo de la crafting grid)

        // Dibujar sobre los 27 slots del inventario (3 filas)
        context.fill(slotStartX - 1, slotStartY - 1, slotStartX + 162, slotStartY + 55, 0xFFC6C6C6);
    }
}