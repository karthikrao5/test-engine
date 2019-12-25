package com.pantheon.core.buffers;

import com.pantheon.core.models.Model;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class BufferModel {
    private Model model;
    private int vaoId;
    private Set<Integer> vbos = new HashSet<>();
    private Set<Integer> textures = new HashSet<>();

    public BufferModel(Model model) {
        this.model = model;
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        storeElementData(model.getTriangles());
        storeDataInAttribute(0, 3, model.getVertices());

        if (model.getTexture() != null) {
            textures.add(model.getTexture().getTextureId());
            storeDataInAttribute(1, 2, model.getTextCoords());
        }
    }

    public void draw() {
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, model.getTexture().getTextureId());
        glDrawElements(GL_TRIANGLES, model.getTriangles().length, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void cleanUp() {
        for (int vbo : vbos) {
            glDeleteVertexArrays(vbo);
        }
        for (int textureId : textures) {
            glDeleteTextures(textureId);
        }
        glDeleteBuffers(vaoId);
    }

    private void storeElementData(int[] indices) {
        int vboId = glGenBuffers();
        vbos.add(vboId);

        IntBuffer bufferData = convertToIntBuffer(indices);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, bufferData, GL_STATIC_DRAW);
    }

    private FloatBuffer convertToFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    private IntBuffer convertToIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    private void storeDataInAttribute(int attributeNumber, int size, float[] data) {
        int vboId = glGenBuffers();
        vbos.add(vboId);

        FloatBuffer bufferData = convertToFloatBuffer(data);

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, bufferData, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, size, GL_FLOAT, false,  0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
