package com.pantheon.core.renderer;

import com.pantheon.core.kernel.Window;
import com.pantheon.core.models.Entity;
import com.pantheon.core.models.TexturedModel;
import com.pantheon.core.shaders.BaseShader;
import com.pantheon.core.utils.MathUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

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
    private BaseShader shader;

    public Renderer(BaseShader shader) {
        this.shader = shader;
        createProjectionMatrix();
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        this.shader.start();
        this.shader.loadProjectionMatrix(projectionMatrix);
        this.shader.stop();
    }


    /**
     * Debug purposes only
     */
    public void render(Entity entity, BaseShader shader) {
        glBindVertexArray(entity.getTexturedModel().getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        Matrix4f transform = MathUtils.createTransformationMatrix(
                entity.getPosition(), entity.getRotX(), entity.getRotY(),
                entity.getRotZ(), entity.getScale());

        shader.loadTransformationMatrix(transform);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, entity.getTexturedModel().getTextureId());
        glDrawElements(GL_TRIANGLES, entity.getTexturedModel().getRawModel().getTriangles().length, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel texturedModel : entities.keySet()) {
            prepareTexturedModel(texturedModel);
            for (Entity entity : entities.get(texturedModel)) {
                prepareInstance(entity);
                entity.rotate(new Vector3f(0, -0.2f, 0));
                glDrawElements(GL_TRIANGLES, entity.getTexturedModel().getRawModel().getTriangles().length, GL_UNSIGNED_INT, 0);
            }

            unbinedTexturedModel();
        }
    }

    public void prepare() {
        glClearColor(0.2f, 0.2f, 0.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glClearDepth(1.0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private void prepareTexturedModel(TexturedModel model) {
        glBindVertexArray(model.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        shader.loadShineVariables(model.getShineDamper(), model.getReflectivity());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, model.getTextureId());
    }

    private void unbinedTexturedModel() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f transform = MathUtils.createTransformationMatrix(
                entity.getPosition(), entity.getRotX(), entity.getRotY(),
                entity.getRotZ(), entity.getScale());

        shader.loadTransformationMatrix(transform);
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
