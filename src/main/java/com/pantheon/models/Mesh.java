package com.pantheon.models;

import org.joml.Vector2f;

public class Mesh {
    private Vector2f[] vertices;
    private int[] triangles;

    public Vector2f[] getVertices() {
        return vertices;
    }

    public void setVertices(Vector2f[] vertices) {
        this.vertices = vertices;
    }

    public int[] getTriangles() {
        return triangles;
    }

    public void setTriangles(int[] triangles) {
        this.triangles = triangles;
    }
}
