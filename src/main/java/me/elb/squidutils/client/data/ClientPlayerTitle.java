package me.elb.squidutils.client.data;

public class ClientPlayerTitle {
    private static boolean enabled;
    private static String title = "";
    private static String subtitle = "";
    private static int titleColor = 0xFFFFFF;
    private static float offsetX = 0.0F;
    private static float offsetY = 0.0F; // Más arriba por defecto
    private static float titleScale = 2.0F; // Título más grande
    private static float subtitleScale = 1.0F; // Subtítulo tamaño normal

    // Sistema de fade out
    private static long displayStartTime = 0L;
    private static long displayDuration = 5000L; // 5 segundos por defecto
    private static long fadeOutDuration = 1000L; // 1 segundo de fade
    private static boolean autoFade = true;

    public static void setEnabled(boolean enabled) {
        ClientPlayerTitle.enabled = enabled;
        if (enabled) {
            displayStartTime = System.currentTimeMillis();
        }
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setTitle(String title) {
        ClientPlayerTitle.title = title;
    }

    public static String getTitle() {
        return title;
    }

    public static void setSubtitle(String subtitle) {
        ClientPlayerTitle.subtitle = subtitle;
    }

    public static String getSubtitle() {
        return subtitle;
    }

    public static void setTitleColor(int color) {
        ClientPlayerTitle. titleColor = color;
    }

    public static int getTitleColor() {
        return titleColor;
    }

    public static void setOffsetX(float offsetX) {
        ClientPlayerTitle. offsetX = offsetX;
    }

    public static float getOffsetX() {
        return offsetX;
    }

    public static void setOffsetY(float offsetY) {
        ClientPlayerTitle.offsetY = offsetY;
    }

    public static float getOffsetY() {
        return offsetY;
    }

    public static void setTitleScale(float scale) {
        ClientPlayerTitle. titleScale = scale;
    }

    public static float getTitleScale() {
        return titleScale;
    }

    public static void setSubtitleScale(float scale) {
        ClientPlayerTitle. subtitleScale = scale;
    }

    public static float getSubtitleScale() {
        return subtitleScale;
    }

    public static void setDisplayDuration(long durationMs) {
        ClientPlayerTitle.displayDuration = durationMs;
    }

    public static long getDisplayDuration() {
        return displayDuration;
    }

    public static void setFadeOutDuration(long durationMs) {
        ClientPlayerTitle.fadeOutDuration = durationMs;
    }

    public static long getFadeOutDuration() {
        return fadeOutDuration;
    }

    public static void setAutoFade(boolean autoFade) {
        ClientPlayerTitle.autoFade = autoFade;
    }

    public static boolean isAutoFade() {
        return autoFade;
    }

    /**
     * Calcula la opacidad actual basada en el tiempo transcurrido
     * @return Valor entre 0.0 (invisible) y 1.0 (completamente visible)
     */
    public static float getCurrentAlpha() {
        if (!enabled || ! autoFade) return 1.0F;

        long elapsed = System.currentTimeMillis() - displayStartTime;

        // Si aún está en el período de display completo
        if (elapsed < displayDuration) {
            return 1.0F;
        }

        // Si está en el período de fade out
        long fadeElapsed = elapsed - displayDuration;
        if (fadeElapsed < fadeOutDuration) {
            return 1.0F - ((float) fadeElapsed / (float) fadeOutDuration);
        }

        // Ya pasó el tiempo, desactivar
        enabled = false;
        return 0.0F;
    }

    public static void setState(boolean enabled, String title, String subtitle, int titleColor,
                                float offsetX, float offsetY, float titleScale, float subtitleScale,
                                long displayDuration, long fadeOutDuration, boolean autoFade) {
        setEnabled(enabled);
        setTitle(title);
        setSubtitle(subtitle);
        setTitleColor(titleColor);
        setOffsetX(offsetX);
        setOffsetY(offsetY);
        setTitleScale(titleScale);
        setSubtitleScale(subtitleScale);
        setDisplayDuration(displayDuration);
        setFadeOutDuration(fadeOutDuration);
        setAutoFade(autoFade);
    }

    public static void clear() {
        enabled = false;
        title = "";
        subtitle = "";
        titleColor = 0xFFFFFF;
        offsetX = 0.0F;
        offsetY = -80.0F;
        titleScale = 2.0F;
        subtitleScale = 1.0F;
        displayStartTime = 0L;
        displayDuration = 5000L;
        fadeOutDuration = 1000L;
        autoFade = true;
    }
}