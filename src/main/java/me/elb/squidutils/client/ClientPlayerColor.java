package me.elb.squidutils.client;

public class ClientPlayerMin {
   private static boolean enabled;
   private static float posX;
   private static float posY;
   private static float sizeX;
   private static float sizeY;

   public static void setEnabled(boolean enabled) {
      ClientPlayerMin.enabled = enabled;
   }

   public static boolean isEnabled() {
      return enabled;
   }

   public static void setPosition(float x, float y) {
      posX = x;
      posY = y;
   }

   public static float getPosX() {
      return posX;
   }

   public static float getPosY() {
      return posY;
   }

   public static void setSize(float x, float y) {
      sizeX = x;
      sizeY = y;
   }

   public static float getSizeX() {
      return sizeX;
   }

   public static float getSizeY() {
      return sizeY;
   }

   public static void setState(boolean enabled, float posX, float posY, float sizeX, float sizeY) {
      setEnabled(enabled);
      setPosition(posX, posY);
      setSize(sizeX, sizeY);
   }
}
