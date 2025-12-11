package me.elb.squidutils.client.data;

public class ClientPlayerColor {
   private static boolean enabled;
   private static float borderWidth = 0.25F;
   private static float blurIntensity = 1.0F;
   private static float colorR = 1.0F;
   private static float colorG = 0.0F;
   private static float colorB = 1.0F;

   public static void setEnabled(boolean enabled) {
      ClientPlayerColor.enabled = enabled;
   }

   public static boolean isEnabled() {
      return enabled;
   }

   public static void setBorderWidth(float width) {
      ClientPlayerColor.borderWidth = width;
   }

   public static float getBorderWidth() {
      return borderWidth;
   }

   public static void setBlurIntensity(float intensity) {
      ClientPlayerColor.blurIntensity = intensity;
   }

   public static float getBlurIntensity() {
      return blurIntensity;
   }

   public static void setColor(float r, float g, float b) {
      colorR = r;
      colorG = g;
      colorB = b;
   }

   public static float getColorR() {
      return colorR;
   }

   public static float getColorG() {
      return colorG;
   }

   public static float getColorB() {
      return colorB;
   }

   public static void setColorFromHex(int hexColor) {
      colorR = ((hexColor >> 16) & 0xFF) / 255.0F;
      colorG = ((hexColor >> 8) & 0xFF) / 255.0F;
      colorB = (hexColor & 0xFF) / 255.0F;
   }

   public static void setState(boolean enabled, float borderWidth, float blurIntensity, float r, float g, float b) {
      setEnabled(enabled);
      setBorderWidth(borderWidth);
      setBlurIntensity(blurIntensity);
      setColor(r, g, b);
   }

   public static void setStateWithHex(boolean enabled, float borderWidth, float blurIntensity, int hexColor) {
      setEnabled(enabled);
      setBorderWidth(borderWidth);
      setBlurIntensity(blurIntensity);
      setColorFromHex(hexColor);
   }
}