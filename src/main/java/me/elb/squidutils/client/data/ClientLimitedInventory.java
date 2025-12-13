package me.elb.squidutils.client.data;

/**
 * Estado del inventario limitado en el cliente
 */
public class ClientLimitedInventory {
    
    private static boolean enabled = false;
    
    public static void setEnabled(boolean state) {
        enabled = state;
    }
    
    public static boolean isEnabled() {
        return enabled;
    }
}