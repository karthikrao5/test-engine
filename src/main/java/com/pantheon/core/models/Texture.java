package com.pantheon.core.models;

public class Texture {
    private int textureId;
    private float[] textCoords;

    public Texture(int textureId) {
        this.textureId = textureId;
    }

    public int getTextureId() {
        return textureId;
    }

    public float[] getTextCoords() {
        return textCoords;
    }

    public void setTextCoords(float[] textCoords) {
        this.textCoords = textCoords;
    }
}
