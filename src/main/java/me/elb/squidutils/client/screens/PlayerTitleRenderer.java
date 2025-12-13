package me.elb.squidutils.client.screens;

import me.elb.squidutils.client.data.ClientPlayerTitle;
import net. minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui. DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft. client.util.math.MatrixStack;

public class PlayerTitleRenderer {

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!ClientPlayerTitle.isEnabled()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        String title = ClientPlayerTitle.getTitle();
        String subtitle = ClientPlayerTitle.getSubtitle();

        if (title.isEmpty() && subtitle.isEmpty()) return;

        // Obtener alpha actual (para fade out)
        float alpha = ClientPlayerTitle.getCurrentAlpha();
        if (alpha <= 0.0F) return;

        TextRenderer textRenderer = client.textRenderer;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int centerX = screenWidth / 2;

        // Posición adaptable:  porcentaje de la altura de la pantalla
        // 0.15 = 15% desde arriba (funciona en cualquier resolución y escala)
        int baseY = (int) (screenHeight * 0.15F);

        float offsetX = ClientPlayerTitle.getOffsetX();
        float offsetY = ClientPlayerTitle.getOffsetY()+15;

        MatrixStack matrices = context. getMatrices();

        // Renderizar título (más grande)
        if (!title. isEmpty()) {
            matrices.push();

            float titleScale = ClientPlayerTitle.getTitleScale();
            int titleColor = ClientPlayerTitle.getTitleColor();

            // Aplicar alpha al color
            int alphaInt = (int) (alpha * 255);
            int titleColorWithAlpha = (alphaInt << 24) | (titleColor & 0x00FFFFFF);
            int shadowColor = (alphaInt << 24);

            // Calcular posición centrada
            int titleWidth = textRenderer.getWidth(title);
            float scaledTitleWidth = titleWidth * titleScale;

            float titleX = centerX - (scaledTitleWidth / 2) + offsetX;
            float titleY = baseY + offsetY;

            // Escalar para el título
            matrices.translate(titleX, titleY, 0);
            matrices.scale(titleScale, titleScale, titleScale);

            // Sombra
            context.drawText(textRenderer, title, 1, 1, shadowColor, false);
            // Texto principal
            context.drawText(textRenderer, title, 0, 0, titleColorWithAlpha, false);

            matrices.pop();
        }

        // Renderizar subtítulo (tamaño normal, debajo del título)
        if (!subtitle. isEmpty()) {
            matrices.push();

            float subtitleScale = ClientPlayerTitle.getSubtitleScale();
            int subtitleColor = 0xFFFFFF; // Siempre blanco

            // Aplicar alpha
            int alphaInt = (int) (alpha * 255);
            int subtitleColorWithAlpha = (alphaInt << 24) | (subtitleColor & 0x00FFFFFF);
            int shadowColor = (alphaInt << 24);

            int subtitleWidth = textRenderer.getWidth(subtitle);
            float scaledSubtitleWidth = subtitleWidth * subtitleScale;

            // Posición:  debajo del título
            float titleHeight = 9 * ClientPlayerTitle.getTitleScale();
            float subtitleX = centerX - (scaledSubtitleWidth / 2) + offsetX;
            float subtitleY = baseY + offsetY + titleHeight + 5;

            matrices. translate(subtitleX, subtitleY, 0);
            matrices.scale(subtitleScale, subtitleScale, subtitleScale);

            // Sombra
            context.drawText(textRenderer, subtitle, 1, 1, shadowColor, false);
            // Texto principal
            context.drawText(textRenderer, subtitle, 0, 0, subtitleColorWithAlpha, false);

            matrices.pop();
        }
    }
}