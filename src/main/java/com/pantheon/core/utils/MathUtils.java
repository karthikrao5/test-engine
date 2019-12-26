package com.pantheon.core.utils;


import com.pantheon.core.camera.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MathUtils {

    private static final Vector3f xAxis = new Vector3f(1,0,0);
    private static final Vector3f yAxis = new Vector3f(0,1,0);
    private static final Vector3f zAxis = new Vector3f(0,0,1);

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f mat = new Matrix4f();
        mat.identity();
        mat.translate(translation, mat);
        mat.rotate((float) Math.toRadians(rx), xAxis, mat);
        mat.rotate((float) Math.toRadians(ry), yAxis, mat);
        mat.rotate((float) Math.toRadians(rz), zAxis, mat);
        mat.scale(new Vector3f(scale, scale, scale), mat);
        return mat;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f mat = new Matrix4f();
        mat.identity();
        mat.rotate((float) Math.toRadians(camera.getPitch()), xAxis, mat);
        mat.rotate((float) Math.toRadians(camera.getYaw()), yAxis, mat);
        mat.rotate((float) Math.toRadians(camera.getRoll()), zAxis, mat);

        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPost = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        mat.translate(negativeCameraPost, mat);
        return mat;
    }
}
