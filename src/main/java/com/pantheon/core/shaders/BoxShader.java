package com.pantheon.core.shaders;

import org.joml.Matrix4f;

public class BoxShader extends ShaderProgram {
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    public BoxShader() {
        super("vertex.glsl", "frag.glsl");
    }

    @Override
    protected void bindAttributes() {
        this.bindAttribute(0, "position");
        this.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }
}
