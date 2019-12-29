package com.pantheon.core.models;

public class TexturedModel {
    private int textureId;
    private RawModel rawModel;

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
}
