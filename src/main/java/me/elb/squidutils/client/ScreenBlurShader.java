package me.elb.squidutils.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

@Environment(EnvType.CLIENT)
public class ScreenMinimizeShader {

    private static ScreenMinimizeShader INSTANCE;

    private final ManagedShaderEffect shaderEffect;
    public boolean enabled = false;
    public Vector3f scale = new Vector3f(1.0F, 1.0F, 1.0F);
    public Vector2f offset = new Vector2f(0.0F, 0.0F);

    private ScreenMinimizeShader() {
        shaderEffect = ShaderEffectManager.getInstance().manage(
                Identifier.tryParse("instrucciones", "shaders/post/minimize.json"),
                shader -> {}
        );
    }

    public static void initialize() {
        if (INSTANCE == null) {
            INSTANCE = new ScreenMinimizeShader();
        }
    }

    public static ScreenMinimizeShader get() {
        if (INSTANCE == null) {
            initialize();
        }
        return INSTANCE;
    }

    public void updateShaderState() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) {
            enabled = false;
            return;
        }

        boolean shouldEnable = ClientPlayerMin.isEnabled();
        if (shouldEnable != enabled) {
            enabled = shouldEnable;
        }
    }

    public void beforeProcess() {
        if (!enabled) return;

        shaderEffect.setUniformValue("WindowPos",
                ClientPlayerMin.getPosX(), ClientPlayerMin.getPosY());
        shaderEffect.setUniformValue("WindowSize",
                ClientPlayerMin.getSizeX(), ClientPlayerMin.getSizeY());
    }

    public void render(float tickDelta) {
        if (enabled) {
            shaderEffect.render(tickDelta);
        }
    }
}