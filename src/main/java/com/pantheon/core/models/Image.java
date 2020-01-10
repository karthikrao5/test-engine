package com.pantheon.core.models;

import com.pantheon.core.buffers.BufferModel;
import org.joml.Vector3f;

public class Image {

    private BufferModel bufferModel;
    private RawModel rawModel;
    private Vector3f position;
    private Vector3f rotation;

    public Image(RawModel model) {
        this.rawModel = model;
        this.bufferModel = new BufferModel(rawModel);
    }

    public BufferModel getBufferModel() {
        return bufferModel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public RawModel getRawModel() {
        return rawModel;
    }
}
