package com.pantheon.core.camera;

import com.pantheon.core.kernel.Input;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {

    private Vector3f position;
    private float pitch = 0f, yaw = 0f, roll = 0f;

    public Camera() {
        this.position = new Vector3f(0,0,0);
    }

    public Camera(Vector3f position) {
        this.position = position;
    }

    public void move() {
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_UP)) {
            this.position.z -= 1.0f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_DOWN)) {
            this.position.z += 1.0f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_LEFT)) {
            this.position.x -= 1.0f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_RIGHT)) {
            this.position.x += 1.0f;
        }


        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_W)) {
            this.position.y += 1.0f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_S)) {
            this.position.y -= 1.0f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_A)) {
            this.pitch += 1.0f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_D)) {
            this.pitch -= 1.0f;
        }
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
