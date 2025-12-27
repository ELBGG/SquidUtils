package me.elb.squidutils.server.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuración de comandos que se ejecutan cuando un jugador muere
 */
public class DeadCommandsConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_PATH = ".config/squidutils/dead.json";

    private static DeadCommandsConfig instance;

    // Lista de comandos programados
    public List<ScheduledCommand> commands = new ArrayList<>();

    /**
     * Comando programado con delay
     */
    public static class ScheduledCommand {
        public double delaySeconds;  // Tiempo en segundos desde la muerte
        public String command;        // Comando a ejecutar ({player} será reemplazado por el nombre del jugador)

        public ScheduledCommand() {
        }

        public ScheduledCommand(double delaySeconds, String command) {
            this.delaySeconds = delaySeconds;
            this.command = command;
        }
    }

    /**
     * Carga o crea la configuración
     */
    public static DeadCommandsConfig load() {
        File configFile = new File(CONFIG_PATH);

        if (!configFile.exists()) {
            // Crear configuración por defecto con ejemplos
            instance = new DeadCommandsConfig();
            instance.createDefaultConfig();
            instance.save();
            System.out.println("[SquidUtils] Configuración de comandos de muerte creada en: " + CONFIG_PATH);
            return instance;
        }

        try (FileReader reader = new FileReader(configFile)) {
            instance = GSON.fromJson(reader, DeadCommandsConfig.class);
            System.out.println("[SquidUtils] Configuración de comandos de muerte cargada desde: " + CONFIG_PATH);
            return instance;
        } catch (IOException e) {
            System.err.println("[SquidUtils] Error al cargar configuración de comandos de muerte: " + e.getMessage());
            instance = new DeadCommandsConfig();
            return instance;
        }
    }

    /**
     * Crea la configuración por defecto con ejemplos
     */
    private void createDefaultConfig() {
        commands.add(new ScheduledCommand(0, "say {player} ha muerto!"));
        commands.add(new ScheduledCommand(3, "title {player} times 10 70 20"));
        commands.add(new ScheduledCommand(3.05, "title {player} title {\"text\":\"HAS MUERTO\",\"color\":\"red\",\"bold\":true}"));
        commands.add(new ScheduledCommand(3.1, "title {player} subtitle {\"text\":\"Mejor suerte la próxima vez\",\"color\":\"gray\"}"));
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
            System.out.println("[SquidUtils] Configuración de comandos de muerte guardada en: " + CONFIG_PATH);
        } catch (IOException e) {
            System.err.println("[SquidUtils] Error al guardar configuración de comandos de muerte: " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia actual
     */
    public static DeadCommandsConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    /**
     * Recarga la configuración desde el disco
     */
    public static void reload() {
        instance = load();
    }
}
