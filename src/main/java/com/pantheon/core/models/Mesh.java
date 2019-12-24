package com.pantheon.core.models;

import org.joml.Vector2f;

public class Mesh {
    private float[] vertices;
    private int[] triangles;

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public int[] getTriangles() {
        return triangles;
    }

    public void setTriangles(int[] triangles) {
        this.triangles = triangles;
    }
}
