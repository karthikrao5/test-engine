package com.pantheon.core.buffers;

import com.pantheon.core.models.RawModel;
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
    private RawModel rawModel;
    private int vaoId;
    private Set<Integer> vbos = new HashSet<>();
    private Set<Integer> textures = new HashSet<>();

    public BufferModel(RawModel rawModel) {
        this.rawModel = rawModel;
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        storeElementData(rawModel.getTriangles());
        storeDataInAttribute(0, 3, rawModel.getVertices());

        if (rawModel.getTextureId() > 0) {
            textures.add(rawModel.getTextureId());
            storeDataInAttribute(1, 2, rawModel.getTextCoords());
        }
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

    public RawModel getRawModel() {
        return rawModel;
    }

    public void setRawModel(RawModel rawModel) {
        this.rawModel = rawModel;
    }
}
