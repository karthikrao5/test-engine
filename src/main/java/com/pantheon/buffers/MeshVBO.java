package com.pantheon.buffers;

import com.pantheon.models.Mesh;

import static org.lwjgl.opengl.GL15.glGenBuffers;

public class MeshVBO implements VBO {
    private int vbo1;
    private int size;

    public MeshVBO() {
        this.vbo1 = glGenBuffers();
        this.size = 0;
    }

    @Override
    public void allocate(Mesh mesh) {
//        get vertex and triangle data and put it into vbo
        size = mesh.getTriangles().length;
    }

    @Override
    public void draw() {

    }

    @Override
    public void delete() {

    }
}
