package me.elb.squidutils.client;

import me.elb.squidutils.client.data.PlayerTitleRenderer;
import me.elb.squidutils.client.shader.ClientDeathFade;
import me.elb.squidutils.client.shader.ScreenBlurShader;
import me.elb.squidutils.net.SquidUtilsNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;

public class SquidutilsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        SquidUtilsNetwork.registerClientReceivers();

        ClientDeathFade.initialize();

        // Registrar shaders en el callback de Satin
        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            // Blur shader
            ScreenBlurShader shader = ScreenBlurShader. get();
            shader.updateShaderState();
            shader.beforeProcess();
            shader.render(tickDelta);

            // Death fade shader (se renderiza después del blur)
            ClientDeathFade deathFade = ClientDeathFade.get();
            deathFade.updateShaderState();
            deathFade.beforeProcess();
            deathFade.render(tickDelta);
        });

        HudRenderCallback.EVENT.register(PlayerTitleRenderer::render);

    }
}
