package dev.sterner.gorelib.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class RenderUtils {
    public static final int FULL_BRIGHT = 15728880;

    /**
     * To allow textures to draw at float x and y
     */
    public static void drawTexture(MatrixStack matrices, float x, float y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        drawTexturedQuad(matrices, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
    }

    public static void drawTexture(MatrixStack matrices, float x, float y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        drawTexture(matrices, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
    }

    private static void drawTexturedQuad(MatrixStack matrices, float x0, float x1, float y0, float y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
        drawTexturedQuad(matrices.peek().getPositionMatrix(), x0, x1, y0, y1, z, (u + 0.0F) / (float) textureWidth, (u + (float) regionWidth) / (float) textureWidth, (v + 0.0F) / (float) textureHeight, (v + (float) regionHeight) / (float) textureHeight);
    }

    private static void drawTexturedQuad(Matrix4f matrix, float x0, float x1, float y0, float y1, int z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix, x0, y0, (float) z).texture(u0, v0).next();
        bufferBuilder.vertex(matrix, x0, y1, (float) z).texture(u0, v1).next();
        bufferBuilder.vertex(matrix, x1, y1, (float) z).texture(u1, v1).next();
        bufferBuilder.vertex(matrix, x1, y0, (float) z).texture(u1, v0).next();
        BufferRenderer.drawWithShader(bufferBuilder.end());
    }

    public static void renderTexture(MatrixStack matrices, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);

        drawTexture(matrices, x, y, u, v, width, height, textureWidth, textureHeight);

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    public static void renderBlockAtPosition(WorldRenderContext context, Vec3d pos, Identifier texture, float alpha) {
        renderBlockAtPosition(context.matrixStack(), context.camera(), pos, texture, alpha);
    }

    /**
     * Renders a Block at a pos
     */
    public static void renderBlockAtPosition(MatrixStack matrixStack, Camera camera, Vec3d pos, Identifier texture, float alpha) {
        matrixStack.push();
        Vec3d transformedPos = pos.subtract(camera.getPos());
        matrixStack.translate(transformedPos.x, transformedPos.y, transformedPos.z);
        Matrix4f positionMatrix = matrixStack.peek().getPositionMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);

        Color color = new Color(255, 255, 255, alpha);
        int intColor = color.getRGB();

        for (Direction direction : Direction.values()) {
            float x1 = direction == Direction.WEST || direction == Direction.DOWN || direction == Direction.NORTH ? 1 : 0;
            float x2 = direction == Direction.EAST || direction == Direction.UP || direction == Direction.SOUTH ? 1 : 0;
            float y1 = direction == Direction.DOWN || direction == Direction.NORTH || direction == Direction.WEST ? 1 : 0;
            float y2 = direction == Direction.UP || direction == Direction.SOUTH || direction == Direction.EAST ? 1 : 0;
            float z1 = direction == Direction.NORTH || direction == Direction.UP || direction == Direction.WEST ? 1 : 0;
            float z2 = direction == Direction.SOUTH || direction == Direction.DOWN || direction == Direction.EAST ? 1 : 0;

            buffer.vertex(positionMatrix, x1, y1, z1).color(intColor).texture(0, 1).next();
            buffer.vertex(positionMatrix, x1, y2, z2).color(intColor).texture(0, 0).next();
            buffer.vertex(positionMatrix, x2, y2, z2).color(intColor).texture(1, 0).next();
            buffer.vertex(positionMatrix, x2, y1, z1).color(intColor).texture(1, 1).next();
        }

        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, texture);
        tessellator.draw();

        matrixStack.pop();
    }
}
