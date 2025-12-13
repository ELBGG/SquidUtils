package me.elb.squidutils.client.screens;

import net.minecraft.client.MinecraftClient;
import net. minecraft.client.gui.DrawContext;
import net.minecraft. text.Text;
import net.minecraft.util.Identifier;
import me.elb.squidutils.client.data.GameStage;

import java.util.ArrayList;
import java.util.List;

public class WaitingHud {
    public static final WaitingHud INSTANCE = new WaitingHud();

    // Identificador de la textura
    private static final Identifier WAIT_TEXTURE = Identifier.of("squidutils", "textures/gui/wait.png");

    private int currentPlayers = 0;
    private int maxPlayers = 10;
    private List<String> playersWaiting = new ArrayList<>();
    private GameStage currentStage = GameStage. LOBBY;

    private WaitingHud() {}

    public void updateData(int current, int max, List<String> players, GameStage stage) {
        this.currentPlayers = current;
        this.maxPlayers = max;
        this. playersWaiting = new ArrayList<>(players);
        this.currentStage = stage;
    }

    public void render(DrawContext context) {
        // Solo renderizar si está en WAITING o STARTING
        if (currentStage != GameStage.WAITING && currentStage != GameStage.STARTING) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client. player == null) return;

        int width = client.getWindow().getScaledWidth();
        int height = client. getWindow().getScaledHeight();

        // Renderizar la textura de fondo wait.png
        // La textura se dibuja en toda la pantalla
        context.drawTexture(WAIT_TEXTURE, 0, 0, 0, 0, width, height, width, height);

        // Título
        String titleText = currentStage == GameStage.STARTING ?  "¡Iniciando!" : "Esperando Jugadores... ";
        Text title = Text.literal(titleText);
        int titleColor = currentStage == GameStage.STARTING ? 0x55FF55 : 0xFFFFFF;

        int titleX = (width - client.textRenderer.getWidth(title)) / 2;
        // Renderizar con sombra para mejor visibilidad
        context.drawText(client.textRenderer, title, titleX, height / 2 - 50, titleColor, true);

        // Contador de jugadores
        Text counter = Text.literal(currentPlayers + " / " + maxPlayers);
        int counterX = (width - client.textRenderer.getWidth(counter)) / 2;
        context.drawText(client.textRenderer, counter, counterX, height / 2 - 20, 0xFFAA00, true);

        // Barra de progreso
        int barWidth = 200;
        int barHeight = 10;
        int barX = (width - barWidth) / 2;
        int barY = height / 2;

        // Fondo de la barra
        context.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF333333);

        // Progreso
        int progress = (int) ((float) currentPlayers / maxPlayers * barWidth);
        context.fill(barX, barY, barX + progress, barY + barHeight, 0xFF55FF55);

        // Borde de la barra
        context.drawBorder(barX, barY, barWidth, barHeight, 0xFFFFFFFF);

        // Lista de jugadores
        if (! playersWaiting.isEmpty()) {
            Text label = Text.literal("Jugadores:");
            int labelX = (width - client.textRenderer.getWidth(label)) / 2;
            context.drawText(client.textRenderer, label, labelX, height / 2 + 25, 0xFFFFFF, true);

            int y = height / 2 + 40;
            int displayedPlayers = 0;
            int maxDisplayPlayers = 10;

            for (String playerName : playersWaiting) {
                if (displayedPlayers >= maxDisplayPlayers) {
                    // Mostrar indicador de más jugadores
                    int remaining = playersWaiting.size() - maxDisplayPlayers;
                    Text more = Text.literal("...  +" + remaining + " más");
                    int moreX = (width - client.textRenderer.getWidth(more)) / 2;
                    context.drawText(client.textRenderer, more, moreX, y, 0xAAAAAA, true);
                    break;
                }

                Text playerText = Text.literal("• " + playerName);
                int x = (width - client.textRenderer.getWidth(playerText)) / 2;
                context.drawText(client.textRenderer, playerText, x, y, 0xFFFFFF, true);
                y += 12;
                displayedPlayers++;

                // Verificar que no se salga de la pantalla
                if (y > height - 50) {
                    break;
                }
            }
        }
    }
}