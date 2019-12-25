package com.pantheon.core.renderer;

import com.pantheon.core.kernel.Window;
import com.pantheon.core.models.Entity;
import com.pantheon.core.shaders.BoxShader;
import com.pantheon.core.shaders.ShaderProgram;
import com.pantheon.core.utils.MathUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private static final float FOV = 70f;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private Matrix4f projectionMatrix;

    public Renderer(BoxShader shader) {
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Entity entity, BoxShader shader) {
        shader.start();

        glBindVertexArray(entity.getBufferModel().getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        Matrix4f transform = MathUtils.createTransformationMatrix(
                entity.getPosition(), entity.getRotX(), entity.getRotY(),
                entity.getRotZ(), entity.getScale());

        shader.loadTransformationMatrix(transform);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, entity.getBufferModel().getModel().getTextureId());
        glDrawElements(GL_TRIANGLES, entity.getBufferModel().getModel().getTriangles().length, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.stop();
    }


    private void createProjectionMatrix() {
        float aspectRatio = (float) Window.getInstance().getWidth() / (float) Window.getInstance().getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;

        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }
}
