package me.elb.squidutils.client.shader;

import net. fabricmc.api.EnvType;
import net.fabricmc. api.Environment;
import net. minecraft.client.MinecraftClient;
import net.minecraft.util. Identifier;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api. managed.ShaderEffectManager;

/**
 * Maneja el efecto de oscurecimiento al morir usando Satin
 */
@Environment(EnvType. CLIENT)
public class ClientDeathFade {

    private static ClientDeathFade INSTANCE;

    private final ManagedShaderEffect shaderEffect;
    private boolean active = false;
    private long startTime = 0;

    // Duraciones en milisegundos
    private static final long FADE_IN_DURATION = 3000;  // 3 segundos para oscurecer
    private static final long BLACK_DURATION = 3000;     // 3 segundos en negro
    private static final long FADE_OUT_DURATION = 500;   // 0.5 segundos para aclarar
    private static final long TOTAL_DURATION = FADE_IN_DURATION + BLACK_DURATION + FADE_OUT_DURATION;

    private ClientDeathFade() {
        shaderEffect = ShaderEffectManager.getInstance().manage(
                Identifier.of("squidutils", "shaders/post/death_face.json"),
                shader -> {}
        );
    }

    public static void initialize() {
        if (INSTANCE == null) {
            INSTANCE = new ClientDeathFade();
        }
    }

    public static ClientDeathFade get() {
        if (INSTANCE == null) {
            initialize();
        }
        return INSTANCE;
    }

    /**
     * Activa el efecto de muerte
     */
    public static void activate() {
        ClientDeathFade instance = get();
        if (instance.active) return;

        instance.active = true;
        instance.startTime = System. currentTimeMillis();
    }

    /**
     * Desactiva el efecto
     */
    public static void deactivate() {
        ClientDeathFade instance = get();
        instance.active = false;
    }

    /**
     * Verifica si está activo
     */
    public static boolean isActive() {
        return get().active;
    }

    /**
     * Actualiza el estado del shader
     */
    public void updateShaderState() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) {
            active = false;
            return;
        }

        if (! active) return;

        long elapsed = System.currentTimeMillis() - startTime;

        // Si terminó el efecto completo, desactivar
        if (elapsed >= TOTAL_DURATION) {
            active = false;
        }
    }

    /**
     * Actualiza los parámetros antes de renderizar
     */
    public void beforeProcess() {
        if (!active) return;

        long elapsed = System.currentTimeMillis() - startTime;
        float progress = calculateProgress(elapsed);

        // Actualizar el uniform del shader
        shaderEffect.setUniformValue("Progress", progress);
    }

    /**
     * Calcula el progreso del fade (0.0 = normal, 1.0 = negro)
     */
    private float calculateProgress(long elapsed) {
        if (elapsed < FADE_IN_DURATION) {
            // Fase 1: Fade in (0.0 -> 1.0)
            return (float) elapsed / FADE_IN_DURATION;
        } else if (elapsed < FADE_IN_DURATION + BLACK_DURATION) {
            // Fase 2: Mantener negro (1.0)
            return 1.0F;
        } else {
            // Fase 3: Fade out (1.0 -> 0.0)
            long fadeOutElapsed = elapsed - FADE_IN_DURATION - BLACK_DURATION;
            return 1.0F - ((float) fadeOutElapsed / FADE_OUT_DURATION);
        }
    }

    /**
     * Renderiza el shader
     */
    public void render(float tickDelta) {
        if (active) {
            shaderEffect.render(tickDelta);
        }
    }
}