package com.pantheon.core.buffers;

import com.pantheon.core.models.RawModel;
import com.pantheon.core.models.TexturedModel;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class BufferModel {
    private int vaoId;
    private Set<Integer> vbos = new HashSet<>();
    private Set<Integer> textures = new HashSet<>();
    private Map<String, Integer> attributeMap = new HashMap<>();

    public BufferModel(RawModel rawModel, int textureId) {
        this(rawModel);
        textures.add(textureId);
        storeDataInAttribute("textures", 1, 2, rawModel.getTextCoords());
    }

    public BufferModel(RawModel rawModel) {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        //generate the vbo
        storeElementData(rawModel.getTriangles());
        storeDataInAttribute("vertices", 0, 3, rawModel.getVertices());

        storeDataInAttribute("normals", 2, 3, rawModel.getNormals());
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

    public int getVaoId() {
        return vaoId;
    }

    public void updateVerticesAndTriangleData(float[] vertices, int[] triangles) {
        IntBuffer triangleBuffer = convertToIntBuffer(triangles);
        FloatBuffer verticesBuffer = convertToFloatBuffer(vertices);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, attributeMap.get("elements"));
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, triangleBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, attributeMap.get("vertices"));
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void storeElementData(int[] indices) {
        int vboId = glGenBuffers();
        vbos.add(vboId);
        attributeMap.put("elements", vboId);
        IntBuffer bufferData = convertToIntBuffer(indices);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, bufferData, GL_STATIC_DRAW);
    }

    private void storeDataInAttribute(String attributeName, int attributeNumber, int size, float[] data) {
        int vboId = glGenBuffers();
        vbos.add(vboId);
        attributeMap.put(attributeName, vboId);
        FloatBuffer bufferData = convertToFloatBuffer(data);

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, bufferData, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, size, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
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
}
