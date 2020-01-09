package com.pantheon.core.renderer;

import com.pantheon.core.models.Terrain;
import com.pantheon.core.models.TexturedModel;
import com.pantheon.core.shaders.TerrainShader;
import com.pantheon.core.utils.MathUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TerrainRenderer {
    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(List<Terrain> terrains) {
        for (Terrain terrain : terrains) {
            prepareTerrain(terrain.getTexturedModel());
            loadModelMatrix(terrain);
            loadHeightScaleMatrix(terrain);
            glDrawElements(GL_TRIANGLES, terrain.getTexturedModel().getRawModel().getTriangles().length, GL_UNSIGNED_INT, 0);

            unbinedTexturedModel();
        }
    }

    private void prepareTerrain(TexturedModel model) {
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

    private void loadHeightScaleMatrix(Terrain terrain) {
        Matrix4f scale = new Matrix4f();
        scale.identity();
        scale._m11(10f);
        shader.loadHeightScale(scale);
    }

    private void loadModelMatrix(Terrain terrain) {
        Matrix4f transform = MathUtils.createTransformationMatrix(
                new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);

        shader.loadTransformationMatrix(transform);
    }
}
