package com.pantheon.shaders;

import com.pantheon.utils.ResourceLoader;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Triangle {
    private ShaderProgram program;
    private int vboId;
    private int vaoId;

    public Triangle() {
        this.program = new ShaderProgram();

        int vertexShaderId = program.addVertexShader(ResourceLoader.loadShader("shaders/vertex.glsl"));
        int fragShaderId = program.addFragShader(ResourceLoader.loadShader("shaders/frag.glsl"));

        program.compileShader();

        glDeleteShader(vertexShaderId);
        glDeleteShader(fragShaderId);

        final float[] vertices = new float[]{
                -0.5f, 0.0f, 0.0f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f
        };

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        FloatBuffer verticesBuff = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuff.put(vertices).flip();

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        glBufferData(GL_ARRAY_BUFFER, verticesBuff, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false,  0, 0);
        glEnableVertexAttribArray(0);
    }

    public void draw() {
        glUseProgram(program.getProgramId());

        glBindVertexArray(vaoId);

        glDrawArrays(GL_TRIANGLES, 0, 3);

        glUseProgram(0);
    }
}
