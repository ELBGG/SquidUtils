package me.elb.squidutils. client;

import net.minecraft. client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net. minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util. math.MatrixStack;

public class PlayerTitleRenderer {

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!ClientPlayerTitle.isEnabled()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        String title = ClientPlayerTitle.getTitle();
        String subtitle = ClientPlayerTitle.getSubtitle();

        if (title.isEmpty() && subtitle.isEmpty()) return;

        TextRenderer textRenderer = client.textRenderer;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Calcular posición central
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        // Aplicar offsets
        float offsetX = ClientPlayerTitle.getOffsetX();
        float offsetY = ClientPlayerTitle.getOffsetY();
        float scale = ClientPlayerTitle.getScale();

        MatrixStack matrices = context.getMatrices();
        matrices.push();

        // Aplicar escala
        matrices.scale(scale, scale, scale);

        // Ajustar posiciones por la escala
        int scaledCenterX = (int) (centerX / scale);
        int scaledCenterY = (int) (centerY / scale);
        int scaledOffsetX = (int) (offsetX / scale);
        int scaledOffsetY = (int) (offsetY / scale);

        // Renderizar título
        if (!title. isEmpty()) {
            int titleWidth = textRenderer.getWidth(title);
            int titleX = scaledCenterX - (titleWidth / 2) + scaledOffsetX;
            int titleY = scaledCenterY + scaledOffsetY;
            int titleColor = ClientPlayerTitle.getTitleColor();

            // Sombra del título
            context. drawText(textRenderer, title, titleX + 1, titleY + 1, 0x000000, false);
            // Título con color
            context.drawText(textRenderer, title, titleX, titleY, titleColor | 0xFF000000, false);
        }

        // Renderizar subtítulo (siempre blanco)
        if (!subtitle.isEmpty()) {
            int subtitleWidth = textRenderer.getWidth(subtitle);
            int subtitleX = scaledCenterX - (subtitleWidth / 2) + scaledOffsetX;
            int subtitleY = scaledCenterY + scaledOffsetY + (int)(12 * scale); // 12 píxeles debajo del título
            int subtitleColor = 0xFFFFFF;

            // Sombra del subtítulo
            context.drawText(textRenderer, subtitle, subtitleX + 1, subtitleY + 1, 0x000000, false);
            // Subtítulo blanco
            context.drawText(textRenderer, subtitle, subtitleX, subtitleY, subtitleColor | 0xFF000000, false);
        }

        matrices. pop();
    }
}