package com.pantheon.core.models;

public class TexturedModel {
    private RawModel rawModel;
    private int textureId;
    private float shineDamper = 1;
    private float reflectivity = 0;

    public TexturedModel(int textureId, RawModel rawModel) {
        this.textureId = textureId;
        this.rawModel = rawModel;
    }

    public int getTextureId() {
        return textureId;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public void setRawModel(RawModel rawModel) {
        this.rawModel = rawModel;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
