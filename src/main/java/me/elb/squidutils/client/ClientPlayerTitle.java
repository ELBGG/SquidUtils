package me.elb.squidutils.client;

public class ClientPlayerTitle {
    private static boolean enabled;
    private static String title = "";
    private static String subtitle = "";
    private static int titleColor = 0xFFFFFF; // Blanco por defecto
    private static float offsetX = 0.0F; // Offset desde el centro
    private static float offsetY = -50.0F; // 50 píxeles arriba del centro por defecto
    private static float scale = 1.0F; // Escala del texto

    public static void setEnabled(boolean enabled) {
        ClientPlayerTitle.enabled = enabled;
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

    public static void setScale(float scale) {
        ClientPlayerTitle.scale = scale;
    }

    public static float getScale() {
        return scale;
    }

    public static void setState(boolean enabled, String title, String subtitle, int titleColor, 
                                float offsetX, float offsetY, float scale) {
        setEnabled(enabled);
        setTitle(title);
        setSubtitle(subtitle);
        setTitleColor(titleColor);
        setOffsetX(offsetX);
        setOffsetY(offsetY);
        setScale(scale);
    }

    public static void clear() {
        enabled = false;
        title = "";
        subtitle = "";
        titleColor = 0xFFFFFF;
        offsetX = 0.0F;
        offsetY = -50.0F;
        scale = 1.0F;
    }
}