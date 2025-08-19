package win.huangyu.cntiertagger.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class RenderHelper {
    public static void drawText(MatrixStack matrices, VertexConsumerProvider vertices, Text text, int x, int y) {
        MinecraftClient.getInstance().textRenderer.draw(
                text,
                x,
                y,
                0xFFFFFF,
                true,
                matrices.peek().getPositionMatrix(),
                vertices,
                TextRenderer.TextLayerType.NORMAL,
                0,
                0xF000F0
        );
    }
}