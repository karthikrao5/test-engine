package com.pantheon.core.models;

import org.j3d.texture.procedural.PerlinNoiseGenerator;

public class Terrain {
    private int SIZE = 800;
    private int VERTEX_COUNT = 256;

    private float x;
    private float z;
    private RawModel model;
    private TexturedModel texturedModel;

    private PerlinNoiseGenerator generator;
    private final float FREQ_CHANGE_AMOUNT = 10f;

    private float freq;
    private float prevFreq;

    private int octaves;
    private int prevOctaves;

    public Terrain(float gridX, float gridZ) {
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        generator = new PerlinNoiseGenerator(12);

        freq = 50;
        octaves = 1;

        prevFreq = 0.0f;
        prevOctaves = 0;
    }

    public void setTexturedModel(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public void incrementFreq() {
        this.freq += FREQ_CHANGE_AMOUNT;
    }

    public void decrementFreq() {
        this.freq -= FREQ_CHANGE_AMOUNT;
        if (this.freq < 0) {
            this.freq = 0.0f;
        }
    }

    public void incrementOctaves() {
        this.octaves++;
    }

    public void decrementOctaves() {
        this.octaves--;
        if (this.octaves < 0) {
            this.octaves = 0;
        }
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    private void generate() {
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] textCoords = new float[count * 2];
        float[] normals = new float[count * 3];
        int[] triangles = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];

        int vertexPointer = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                float x = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
                float z = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer * 3] = x;

                float yNoise = 0.0f;

                float gain = 1.0f;
                for (int o = 0; o < octaves; o++) {
                    yNoise += generator.noise2(gain / freq * x, gain / freq * z) * (1 / gain);
                    gain *= 2.0f;
                }

                vertices[vertexPointer * 3 + 1] = yNoise;
                vertices[vertexPointer * 3 + 2] = z;
                textCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
                textCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }

        int pointer = 0;
        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;
                triangles[pointer++] = topLeft;
                triangles[pointer++] = bottomLeft;
                triangles[pointer++] = topRight;
                triangles[pointer++] = topRight;
                triangles[pointer++] = bottomLeft;
                triangles[pointer++] = bottomRight;
            }
        }

        if (this.model != null) {
            this.model.setVertices(vertices);
            this.model.setTriangles(triangles);
        } else {
            this.model = new RawModel(vertices, triangles, textCoords, normals);
        }
    }

    public void generateTerrain() {
        if (freq != prevFreq || octaves != prevOctaves) {
            System.out.println(String.format("Octaves: %d | Freq: %f", octaves, freq));
            generate();
            if (texturedModel != null) {
                texturedModel.updateModelData(this.model);
            }
            prevFreq = freq;
            prevOctaves = octaves;
        }
    }

    public RawModel getModel() {
        return model;
    }
}
