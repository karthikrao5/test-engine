package com.pantheon.core.renderer;

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
}
