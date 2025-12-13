package me.elb.squidutils.client;

import me.elb.squidutils.client. screens.PlayerTitleRenderer;
import me.elb.squidutils.client.screens.WaitingHud;
import me. elb.squidutils.client.shader.ClientDeathFade;
import me.elb. squidutils.client.shader. ScreenBlurShader;
import me.elb.squidutils.net.SquidUtilsNetwork;
import me.elb.squidutils. net.SyncWaitingRoomPayload;
import net. fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client. rendering.v1.HudRenderCallback;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;

public class SquidutilsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        System.out.println("[SquidUtils] Initializing client...");

        // ✅ Registrar receptores de red existentes
        SquidUtilsNetwork.registerClientReceivers();

        // ✅ NUEVO: Registrar el receptor del payload de waiting room
        SyncWaitingRoomPayload.registerClientReceiver();
        System.out.println("[SquidUtils] Waiting room payload receiver registered");

        // ✅ Inicializar efectos de muerte
        ClientDeathFade. initialize();

        // ✅ Registrar shaders en el callback de Satin
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

        // ✅ Registrar HUD renders
        HudRenderCallback. EVENT.register((drawContext, tickCounter) -> {
            PlayerTitleRenderer.render(drawContext, tickCounter);
            WaitingHud.INSTANCE.render(drawContext);
        });

        System.out.println("[SquidUtils] Client initialized successfully!");
    }
}