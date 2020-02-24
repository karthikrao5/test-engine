package com.pantheon.core.models;

import org.j3d.texture.procedural.PerlinNoiseGenerator;

import java.util.Random;

public class Terrain {
    private Random rand = new Random();

    private int SIZE = 800;
    private int VERTEX_COUNT = 256;

    private float x;
    private float z;
    private RawModel model;
    private TexturedModel texturedModel;

    private PerlinNoiseGenerator generator;
    private final float DELTA_SCALE = 0.1f;
    private final float LAC_CHANGE_AMOUNT = 0.1f;
    private final float PERSISTANCE_CHANGE_AMOUNT = 0.01f;

    private float scale;
    private float prevScale;

    private float lacunarity;
    private float prevLacunarity;

    private float persistence;
    private float prevPersistence;

    private int octaves;
    private int prevOctaves;

    private int seed;
    private int prevSeed;

    public Terrain(float gridX, float gridZ) {
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        
        this.randomSeed();

        scale = 1.0f;
        prevScale = 1.0f;

        octaves = 1;
        prevOctaves = 0;

        lacunarity = 1.0f;
        prevLacunarity = 0f;

        persistence = 0.5f;
        prevPersistence = 0.0f;
    }

    public void setTexturedModel(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public void incScale() {
        this.scale += DELTA_SCALE;
    }

    public void decScale() {
        this.scale -= DELTA_SCALE;
        if (this.scale < 0) {
            this.scale = 0.0f;
        }
    }

    public void incrementOctaves() {
        this.octaves++;
    }

    public void decrementOctaves() {
        this.octaves--;
        if (this.octaves <= 0) {
            this.octaves = 1;
        }
    }

    public void incrementLacunarity() {
        this.lacunarity += LAC_CHANGE_AMOUNT;
    }

    public void decrementLacunarity() {
        this.lacunarity -= LAC_CHANGE_AMOUNT;

        if (this.lacunarity < 1) {
            this.lacunarity = 1.0f;
        }
    }

    public void incrementPersistance() {
        this.persistence += PERSISTANCE_CHANGE_AMOUNT;
        if (this.persistence > 1.0f) {
            this.persistence = 1.0f;
        }
    }

    public void decrementPersistance() {
        this.persistence -= PERSISTANCE_CHANGE_AMOUNT;

        if (this.persistence < 0) {
            this.persistence = 0.01f;
        }
    }

    public void randomSeed() {
        this.seed = rand.nextInt(10000);
        this.generator = new PerlinNoiseGenerator(seed);
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

                float maxVal = 0f;
                float yNoise = 0f;
                float amplitude = 1.0f;
                float freq = 1.0f;
                for (int o = 0; o < octaves; o++) {
                    float perlin = generator.noise2(x / scale * freq, z / scale * freq) * 2 - 1;
                    yNoise += perlin * amplitude;

                    maxVal += amplitude;

                    amplitude *= persistence;
                    freq *= lacunarity;
                }

                vertices[vertexPointer * 3 + 1] = (yNoise / maxVal);
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
        if (scale != prevScale || octaves != prevOctaves || lacunarity != prevLacunarity || persistence != prevPersistence || seed != prevSeed) {
            System.out.println(String.format("Octaves: %d | Scale: %f | Lacunarity: %f | Persistance: %f", octaves, scale, lacunarity, persistence));
            generate();
            if (texturedModel != null) {
                texturedModel.updateModelData(this.model);
            }
            prevScale = scale;
            prevOctaves = octaves;
            prevLacunarity = lacunarity;
            prevPersistence = persistence;
            prevSeed = seed;
        }
    }

    public RawModel getModel() {
        return model;
    }
}
