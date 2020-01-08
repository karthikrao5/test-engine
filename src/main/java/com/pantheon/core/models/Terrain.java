package com.pantheon.core.models;

import com.pantheon.core.utils.ResourceLoader;

public class Terrain {
    private int SIZE = 800;
    private int VERTEX_COUNT = 128;

    private float x;
    private float z;
    private RawModel model;
    private TexturedModel texturedModel;

    public Terrain(float gridX, float gridZ) {
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;

        generateTerrain();
    }

    public void setTexturedModel(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    private void generateTerrain() {
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] textCoords = new float[count * 2];
        float[] normals = new float[count * 3];
        int[] triangles = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];

        int vertexPointer = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer * 3 + 1] = 0;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
                normals[vertexPointer * 3] = 0;
                normals[vertexPointer * 3 + 1] = 1;
                normals[vertexPointer * 3 + 2] = 0;
                textCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
                textCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }

        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                triangles[pointer++] = topLeft;
                triangles[pointer++] = bottomLeft;
                triangles[pointer++] = topRight;
                triangles[pointer++] = topRight;
                triangles[pointer++] = bottomLeft;
                triangles[pointer++] = bottomRight;
            }
        }

        this.model = new RawModel(vertices, triangles, textCoords, normals);
    }

    public RawModel getModel() {
        return model;
    }
}
