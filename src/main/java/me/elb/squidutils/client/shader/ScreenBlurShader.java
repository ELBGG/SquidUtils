package me.elb.squidutils.client.shader;

import me.elb.squidutils.client.data.ClientPlayerColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util. Identifier;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api. managed.ShaderEffectManager;

@Environment(EnvType. CLIENT)
public class ScreenBlurShader {

    private static ScreenBlurShader INSTANCE;

    private final ManagedShaderEffect shaderEffect;
    public boolean enabled = false;

    private ScreenBlurShader() {
        shaderEffect = ShaderEffectManager.getInstance().manage(
                Identifier.tryParse("squidutils", "shaders/post/border_blur.json"),
                shader -> {}
        );
    }

    public static void initialize() {
        if (INSTANCE == null) {
            INSTANCE = new ScreenBlurShader();
        }
    }

    public static ScreenBlurShader get() {
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

        boolean shouldEnable = ClientPlayerColor.isEnabled();
        if (shouldEnable != enabled) {
            enabled = shouldEnable;
        }
    }

    public void beforeProcess() {
        if (!enabled) return;

        // Actualizar los parámetros del shader desde ClientPlayerColor
        shaderEffect. setUniformValue("BorderWidth", ClientPlayerColor.getBorderWidth());
        shaderEffect.setUniformValue("BlurIntensity", ClientPlayerColor.getBlurIntensity());
        shaderEffect.setUniformValue("TintColor",
                ClientPlayerColor.getColorR(),
                ClientPlayerColor.getColorG(),
                ClientPlayerColor.getColorB());
    }

    public void render(float tickDelta) {
        if (enabled) {
            shaderEffect.render(tickDelta);
        }
    }
}