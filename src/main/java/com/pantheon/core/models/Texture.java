package com.pantheon.core.models;

public class Texture {
    private int textureId;
    private float reflectivity = 1f;
    private float shineDamper = 0;


    public Texture(int textureId) {
        this.textureId = textureId;
    }

    public int getTextureId() {
        return textureId;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    @Override
    public String toString() {
        return "Texture{" +
                "textureId=" + textureId +
                ", reflectivity=" + reflectivity +
                ", shineDamper=" + shineDamper +
                '}';
    }
}
