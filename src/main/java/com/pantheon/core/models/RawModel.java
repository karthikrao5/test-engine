package com.pantheon.core.models;


public class RawModel {
    private float[] vertices;
    private int[] triangles;
    private float[] normals;
    private float[] textCoords;

    public RawModel(float[] vertices, int[] triangles, float[] textCoords, float[] normals) {
        this.vertices = vertices;
        this.triangles = triangles;
        this.textCoords = textCoords;
        this.normals = normals;
    }

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

    public float[] getTextCoords() {
        return textCoords;
    }

    public void setTextCoords(float[] textCoords) {
        this.textCoords = textCoords;
    }

    public float[] getNormals() {
        return normals;
    }

    public void setNormals(float[] normals) {
        this.normals = normals;
    }
}
