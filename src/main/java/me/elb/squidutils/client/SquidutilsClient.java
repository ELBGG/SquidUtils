package me.elb.squidutils.client;

import me.elb.squidutils.net.SquidUtilsNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;

public class SquidutilsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        SquidUtilsNetwork.registerClientReceivers();

        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            ScreenBlurShader shader = ScreenBlurShader.get();
            shader.updateShaderState();
            shader.beforeProcess();
            shader.render(tickDelta);
        });

        HudRenderCallback.EVENT.register(PlayerTitleRenderer::render);

    }
}
