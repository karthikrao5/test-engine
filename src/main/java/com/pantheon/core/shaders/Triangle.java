package com.pantheon.core.shaders;

import com.pantheon.core.buffers.MeshVBO;
import com.pantheon.core.models.Mesh;

public class Triangle extends ShaderProgram {
    private MeshVBO buffer;

    public Triangle() {
        super("shaders/vertex.glsl", "shaders/frag.glsl");

        final float[] vertices = new float[]{
            -0.5f, 0.5f, 0.0f, //topp left
            0.5f, 0.5f, 0.0f, //top right
            0.5f, -0.5f, 0.0f, //bottom right
        };

        Mesh mesh = new Mesh();
        mesh.setVertices(vertices);

        buffer = new MeshVBO();
        buffer.allocate(mesh);
    }

    public void draw() {
        this.start();
        buffer.draw();
        this.stop();
    }

    @Override
    protected void bindAttributes() {
        this.bindAttribute(0, "position");
    }
}
