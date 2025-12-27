package me.elb.squidutils.client.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import me.elb.squidutils.client.data.GameStage;
import me.elb.squidutils.client.data.ClientWaitingHudConfig;

import java.util.ArrayList;
import java.util.List;

public class WaitingHud {
    public static final WaitingHud INSTANCE = new WaitingHud();

    // Identificador de la textura
    private static final Identifier WAIT_TEXTURE = Identifier.of("squidutils", "textures/gui/wait.png");

    private int currentPlayers = 0;
    private int maxPlayers = 10;
    private List<String> playersWaiting = new ArrayList<>();
    private GameStage currentStage = GameStage.LOBBY;

    // Animación
    private long lastUpdateTime = 0;
    private float animationProgress = 0.0F;

    private WaitingHud() {}

    public void updateData(int current, int max, List<String> players, GameStage stage) {
        this.currentPlayers = current;
        this.maxPlayers = max;
        this.playersWaiting = new ArrayList<>(players);
        this.currentStage = stage;
    }

    public void render(DrawContext context) {
        // Solo renderizar si está en WAITING o STARTING
        if (currentStage != GameStage.WAITING && currentStage != GameStage.STARTING) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        // Renderizar la textura de fondo wait.png (que incluye los símbolos de Squid Game)
        context.drawTexture(WAIT_TEXTURE, 0, 0, 0, 0, width, height, width, height);

        // Actualizar animación
        updateAnimation();

        // Texto principal
        renderMainText(context, width, height, client);

        // Contador de jugadores
        renderCounter(context, width, height, client);
    }

    private void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        if (lastUpdateTime == 0) {
            lastUpdateTime = currentTime;
        }

        float deltaTime = (currentTime - lastUpdateTime) / 1000.0F;
        lastUpdateTime = currentTime;

        // Animación de puntos suspensivos
        animationProgress += deltaTime * 2.0F; // 2 ciclos por segundo
        if (animationProgress > 3.0F) {
            animationProgress = 0.0F;
        }
    }

    private void renderMainText(DrawContext context, int width, int height, MinecraftClient client) {
        boolean isStarting = currentStage == GameStage.STARTING;
        String baseText = isStarting ? "Iniciando" : "Esperando jugadores";

        // Añadir puntos animados
        int dots = (int)animationProgress;
        String dotsStr = ".".repeat(dots);
        String fullText = baseText + dotsStr;

        // Obtener configuración
        float scale = ClientWaitingHudConfig.getMainTextScale();
        int textColor = ClientWaitingHudConfig.getMainTextColor(isStarting);
        int shadowColor = ClientWaitingHudConfig.getMainTextShadowColor();
        float positionY = ClientWaitingHudConfig.getMainTextY();

        // Calcular ancho del texto escalado
        int baseWidth = client.textRenderer.getWidth(fullText);
        int scaledWidth = (int)(baseWidth * scale);

        // Centrar perfectamente
        int textX = (width - scaledWidth) / 2;
        int textY = (int)(height * positionY);

        // Dibujar con escala
        context.getMatrices().push();
        context.getMatrices().translate(textX, textY, 0);
        context.getMatrices().scale(scale, scale, scale);

        // Sombra
        context.drawText(client.textRenderer, fullText, 2, 2, shadowColor, false);
        // Texto principal
        context.drawText(client.textRenderer, fullText, 0, 0, textColor, false);

        context.getMatrices().pop();
    }

    private void renderCounter(DrawContext context, int width, int height, MinecraftClient client) {
        String counterText = currentPlayers + "/" + maxPlayers;

        // Obtener configuración
        float scale = ClientWaitingHudConfig.getCounterScale();
        int textColor = ClientWaitingHudConfig.getCounterColor();
        int shadowColor = ClientWaitingHudConfig.getCounterShadowColor();
        float mainTextY = ClientWaitingHudConfig.getMainTextY();
        int offsetY = ClientWaitingHudConfig.getCounterOffsetY();

        // Calcular ancho del texto escalado
        int baseWidth = client.textRenderer.getWidth(counterText);
        int scaledWidth = (int)(baseWidth * scale);

        // Centrar perfectamente
        int textX = (width - scaledWidth) / 2;
        int textY = (int)(height * mainTextY) + offsetY;

        // Dibujar con escala
        context.getMatrices().push();
        context.getMatrices().translate(textX, textY, 0);
        context.getMatrices().scale(scale, scale, scale);

        // Sombra
        context.drawText(client.textRenderer, counterText, 2, 2, shadowColor, false);
        // Texto principal
        context.drawText(client.textRenderer, counterText, 0, 0, textColor, false);

        context.getMatrices().pop();
    }
}
