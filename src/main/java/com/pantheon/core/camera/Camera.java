package com.pantheon.core.camera;

import com.pantheon.core.kernel.Input;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {

    private static final float MOUSE_SENSITIVITY = 1;
    private Vector3f position;
    private float pitch = 0f, yaw = 0f, roll = 0f;

    public Camera() {
        this.position = new Vector3f(0,0,0);
    }

    public Camera(Vector3f position) {
        this.position = position;
    }

    public void move() {
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_S)) {
            this.position.z += 1.0f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_W)) {
            this.position.z -= 1.0f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_A)) {
            this.position.x -= 1.0f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_D)) {
            this.position.x += 1.0f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_Z)) {
            this.position.y += 1.0f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_X)) {
            this.position.y -= 1.0f;
        }

        // Update camera based on mouse
        if (Input.getInstance().isRightButtonPressed()) {
            Vector2f rotVec = Input.getInstance().getDisplVec();
            moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
    }

    private void moveRotation(float x, float y, float z) {
        this.pitch += x;
        this.yaw += y;
        this.roll += z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
