package com.pantheon.core.shaders;

import org.joml.Matrix4f;

public class BoxShader extends ShaderProgram {
    private int location_transformationMatrix;
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
        location_transformationMatrix = super.getUniformLocation("trans");
        System.out.println("Location matrix code: " + location_transformationMatrix);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }
}
