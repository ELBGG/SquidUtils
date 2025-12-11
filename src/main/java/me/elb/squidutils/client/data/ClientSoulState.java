package me.elb.squidutils.client.data;

/**
 * Estado del alma en el cliente
 */
public class ClientSoulState {
    
    private static boolean inSoulState = false;
    
    public static void setSoulState(boolean state) {
        inSoulState = state;
    }
    
    public static boolean isInSoulState() {
        return inSoulState;
    }
}