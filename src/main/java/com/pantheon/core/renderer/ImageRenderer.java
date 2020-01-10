package com.pantheon.core.renderer;

import com.pantheon.core.models.Image;
import com.pantheon.core.shaders.BaseShader;
import com.pantheon.core.shaders.ImageShader;
import com.pantheon.core.utils.MathUtils;
import org.joml.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class ImageRenderer {
    private ImageShader shader;

    public ImageRenderer(ImageShader imageShader, Matrix4f projectionMatrix) {
        this.shader = imageShader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Debug purposes only
     */
    public void render(List<Image> images) {
        for (Image image : images) {
            glBindVertexArray(image.getBufferModel().getVaoId());
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);

            Matrix4f transform = MathUtils.createTransformationMatrix(
                    image.getPosition(), image.getRotation().x, image.getRotation().y,
                    image.getRotation().z, 1f);

            shader.loadTransformationMatrix(transform);

            glDrawElements(GL_TRIANGLES, image.getRawModel().getTriangles().length, GL_UNSIGNED_INT, 0);
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glBindVertexArray(0);
        }
    }
}
