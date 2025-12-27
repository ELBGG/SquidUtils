package me.elb.squidutils.client.data;

/**
 * Almacena la configuración del WaitingHud en el cliente
 */
public class ClientWaitingHudConfig {

    private static int mainTextColor = 0xFFFFFFFF;
    private static int mainTextShadowColor = 0xFF000000;
    private static int counterColor = 0xFFFFFFFF;
    private static int counterShadowColor = 0xFF000000;
    private static int startingTextColor = 0xFF55FF55;

    private static float mainTextScale = 2.0F;
    private static float counterScale = 1.5F;
    private static float mainTextY = 0.45F;
    private static int counterOffsetY = 40;

    /**
     * Actualiza la configuración desde el servidor
     */
    public static void updateConfig(
        int mainText,
        int mainTextShadow,
        int counter,
        int counterShadow,
        int startingText,
        float mainScale,
        float counterScaleVal,
        float mainY,
        int counterOffset
    ) {
        mainTextColor = mainText;
        mainTextShadowColor = mainTextShadow;
        counterColor = counter;
        counterShadowColor = counterShadow;
        startingTextColor = startingText;
        mainTextScale = mainScale;
        counterScale = counterScaleVal;
        mainTextY = mainY;
        counterOffsetY = counterOffset;
    }

    // Getters
    public static int getMainTextColor(boolean isStarting) {
        return isStarting ? startingTextColor : mainTextColor;
    }

    public static int getMainTextShadowColor() {
        return mainTextShadowColor;
    }

    public static int getCounterColor() {
        return counterColor;
    }

    public static int getCounterShadowColor() {
        return counterShadowColor;
    }

    public static float getMainTextScale() {
        return mainTextScale;
    }

    public static float getCounterScale() {
        return counterScale;
    }

    public static float getMainTextY() {
        return mainTextY;
    }

    public static int getCounterOffsetY() {
        return counterOffsetY;
    }
}
