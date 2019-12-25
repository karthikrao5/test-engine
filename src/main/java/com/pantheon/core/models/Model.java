package com.pantheon.core.models;


public class Model {
    private float[] vertices;
    private int[] triangles;
    private float[] textCoords;
    private Texture texture;

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

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public float[] getTextCoords() {
        return textCoords;
    }

    public void setTextCoords(float[] textCoords) {
        this.textCoords = textCoords;
    }
}
