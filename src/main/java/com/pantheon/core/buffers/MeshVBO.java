package com.pantheon.core.buffers;

import com.pantheon.core.models.Mesh;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class MeshVBO implements VBO {
    private int vaoId;
    private int vbo1;
    private int size;

    public MeshVBO() {
        this.vbo1 = glGenBuffers();
        this.size = 0;

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
    }

    @Override
    public void allocate(Mesh mesh) {
        size = mesh.getVertices().length;
        FloatBuffer verticesBuff = BufferUtils.createFloatBuffer(this.size);
        verticesBuff.put(mesh.getVertices()).flip();

        vbo1 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo1);

        glBufferData(GL_ARRAY_BUFFER, verticesBuff, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false,  0, 0);
        glEnableVertexAttribArray(0);
    }

    @Override
    public void draw() {
        glBindVertexArray(vaoId);
        glDrawArrays(GL_TRIANGLES, 0, this.size);
        glBindVertexArray(0);
    }

    @Override
    public void delete() {

    }
}
