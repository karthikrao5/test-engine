package com.pantheon.core.shaders;

import com.pantheon.core.camera.Camera;
import com.pantheon.core.utils.MathUtils;
import org.joml.Matrix4f;

public class BoxShader extends ShaderProgram {
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    public BoxShader() {
        super("/shaders/vertex.glsl", "/shaders/frag.glsl");
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
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        System.out.printf("trans: %d, proj: %d, view: %d \n", location_transformationMatrix, location_projectionMatrix, location_viewMatrix);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = MathUtils.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, matrix);
    }
}
