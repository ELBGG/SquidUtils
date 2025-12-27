package me.elb.squidutils.server.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Configuración del WaitingHud
 */
public class WaitingHudConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_PATH = "config/squidutils/waitinghud.json";

    private static WaitingHudConfig instance;

    // Colores en formato hexadecimal
    public ColorSettings colors = new ColorSettings();
    public TextScaleSettings textScale = new TextScaleSettings();
    public PositionSettings position = new PositionSettings();

    public static class ColorSettings {
        public String mainText = "#FFFFFF";
        public String mainTextShadow = "#000000";
        public String counter = "#FFFFFF";
        public String counterShadow = "#000000";
        public String startingText = "#55FF55";
    }

    public static class TextScaleSettings {
        public float mainText = 2.0F;
        public float counter = 1.5F;
    }

    public static class PositionSettings {
        public float mainTextY = 0.45F;
        public int counterOffsetY = 40;
    }

    /**
     * Carga o crea la configuración
     */
    public static WaitingHudConfig load() {
        File configFile = new File(CONFIG_PATH);

        if (!configFile.exists()) {
            // Crear configuración por defecto
            instance = new WaitingHudConfig();
            instance.save();
            System.out.println("[SquidUtils] Configuración creada en: " + CONFIG_PATH);
            return instance;
        }

        try (FileReader reader = new FileReader(configFile)) {
            instance = GSON.fromJson(reader, WaitingHudConfig.class);
            System.out.println("[SquidUtils] Configuración cargada desde: " + CONFIG_PATH);
            return instance;
        } catch (IOException e) {
            System.err.println("[SquidUtils] Error al cargar configuración: " + e.getMessage());
            instance = new WaitingHudConfig();
            return instance;
        }
    }

    /**
     * Guarda la configuración
     */
    public void save() {
        File configFile = new File(CONFIG_PATH);

        // Crear directorios si no existen
        configFile.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(this, writer);
            System.out.println("[SquidUtils] Configuración guardada en: " + CONFIG_PATH);
        } catch (IOException e) {
            System.err.println("[SquidUtils] Error al guardar configuración: " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia actual
     */
    public static WaitingHudConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    /**
     * Convierte un color hexadecimal a entero ARGB
     */
    public static int hexToInt(String hex) {
        // Si no tiene alpha, agregar FF al inicio
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        if (hex.length() == 6) {
            hex = "FF" + hex; // Agregar alpha completa
        }

        return (int) Long.parseLong(hex, 16);
    }
}
