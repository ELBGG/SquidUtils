package me.elb. squidutils.server.limitedinventory;

import java.util.HashSet;
import java.util. Set;
import java.util.UUID;

/**
 * Sistema para limitar el inventario de jugadores (solo hotbar + armadura)
 */
public class LimitedInventorySystem {

    private static boolean active = false;
    private static final Set<UUID> limitedPlayers = new HashSet<>();

    /**
     * Activa el sistema
     */
    public static void activate() {
        active = true;
    }

    /**
     * Desactiva el sistema
     */
    public static void deactivate() {
        active = false;
        limitedPlayers.clear();
    }

    /**
     * Verifica si está activo
     */
    public static boolean isActive() {
        return active;
    }

    /**
     * Activa inventario limitado para un jugador específico
     */
    public static void enableForPlayer(UUID playerUuid) {
        limitedPlayers.add(playerUuid);
    }

    /**
     * Desactiva inventario limitado para un jugador específico
     */
    public static void disableForPlayer(UUID playerUuid) {
        limitedPlayers. remove(playerUuid);
    }

    /**
     * Verifica si un jugador tiene inventario limitado
     */
    public static boolean isPlayerLimited(UUID playerUuid) {
        return active && limitedPlayers.contains(playerUuid);
    }

    /**
     * Verifica si un slot está permitido
     * Hotbar: 0-8
     * Armadura: 36-39 (pies, piernas, pecho, cabeza)
     * Offhand: 40
     */
    public static boolean isSlotAllowed(int slot) {
        // Hotbar (slots 0-8)
        if (slot >= 0 && slot <= 8) {
            return true;
        }
        
        // Armadura (slots 36-39 en PlayerInventory)
        // Pero en el ScreenHandler son slots diferentes
        if (slot >= 5 && slot <= 8) { // Slots de armadura en la GUI
            return true;
        }
        
        // Offhand (slot 45 en ScreenHandler)
        if (slot == 45) {
            return true;
        }
        
        return false;
    }

    /**
     * Verifica si un slot está permitido (versión para ScreenHandler)
     */
    public static boolean isScreenHandlerSlotAllowed(int slot) {
        // En PlayerScreenHandler: 
        // 0-8: Hotbar
        // 5-8: Armadura (helmet, chestplate, leggings, boots)
        // 45: Offhand
        
        return (slot >= 0 && slot <= 8) ||  // Hotbar
               (slot >= 5 && slot <= 8) ||  // Armadura
               (slot == 45);                 // Offhand
    }
}