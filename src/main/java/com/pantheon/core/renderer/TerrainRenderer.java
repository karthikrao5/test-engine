package com.pantheon.core.renderer;

import com.pantheon.core.kernel.Input;
import com.pantheon.core.models.Terrain;
import com.pantheon.core.models.TexturedModel;
import com.pantheon.core.shaders.TerrainShader;
import com.pantheon.core.utils.MathUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.nio.IntBuffer;
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
            terrain.generateTerrain();

            prepareTerrain(terrain.getTexturedModel());
            loadModelMatrix(terrain);
            loadHeightScaleMatrix(terrain);

            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

            glDrawElements(GL_TRIANGLES, terrain.getTexturedModel().getRawModel().getTriangles().length, GL_UNSIGNED_INT, 0);

            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            unbinedTexturedModel();
        }
    }

    private void prepareTerrain(TexturedModel model) {
        glBindVertexArray(model.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        shader.loadShineVariables(model.getShineDamper(), model.getReflectivity());
//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, model.getTextureId());
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

    public void update(List<Terrain> terrains) {
        Input input = Input.getInstance();
        if (input.isKeyPushed(GLFW.GLFW_KEY_F) || input.isKeyPushed(GLFW.GLFW_KEY_F)) {
            for(Terrain terrain : terrains) {
                terrain.incrementFreq();
            }
        }

        if (input.isKeyPushed(GLFW.GLFW_KEY_G) || input.isKeyPushed(GLFW.GLFW_KEY_G)) {
            for(Terrain terrain : terrains) {
                terrain.decrementFreq();
            }
        }

        if (input.isKeyPushed(GLFW.GLFW_KEY_O)  || input.isKeyPushed(GLFW.GLFW_KEY_O)) {
            for(Terrain terrain : terrains) {
                terrain.incrementOctaves();
            }
        }

        if (input.isKeyPushed(GLFW.GLFW_KEY_P) || input.isKeyPushed(GLFW.GLFW_KEY_P)) {
            for(Terrain terrain : terrains) {
                terrain.decrementOctaves();
            }
        }
    }
}
