package com.pantheon.core.buffers;

import com.pantheon.core.models.RawModel;
import com.pantheon.core.models.TexturedModel;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class BufferModel {
    private TexturedModel texturedModel;
    private int vaoId;
    private Set<Integer> vbos = new HashSet<>();
    private Set<Integer> textures = new HashSet<>();

    public BufferModel(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        storeElementData(texturedModel.getRawModel().getTriangles());
        storeDataInAttribute(0, 3, texturedModel.getRawModel().getVertices());

        if (texturedModel.getTextureId() > 0) {
            textures.add(texturedModel.getTextureId());
            textures.add(texturedModel.getTextureId());
            storeDataInAttribute(1, 2, texturedModel.getRawModel().getTextCoords());
        }

        storeDataInAttribute(2, 3, texturedModel.getRawModel().getNormals());
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

    public int getVaoId() {
        return vaoId;
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public void setTexturedModel(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }
}
