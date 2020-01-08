package com.pantheon.core.shaders;

import com.pantheon.core.camera.Camera;
import com.pantheon.core.kernel.Light;
import com.pantheon.core.utils.MathUtils;
import org.joml.Matrix4f;

public class BaseShader extends ShaderProgram {
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;

    public BaseShader() {
        super("/shaders/vertex.glsl", "/shaders/frag.glsl");
    }

    @Override
    protected void bindAttributes() {
        this.bindAttribute(0, "position");
        this.bindAttribute(1, "textureCoords");
        this.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");

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

    public void loadLight(Light light) {
        super.loadVector3f(location_lightPosition, light.getPosition());
        super.loadVector3f(location_lightColor, light.getColor());
    }

    public void loadShineVariables(float shineDamper, float reflectivity) {
        super.loadFloat(location_shineDamper, shineDamper);
        super.loadFloat(location_reflectivity, reflectivity);
    }
}
