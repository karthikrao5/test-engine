package com.pantheon.core.camera;

import com.pantheon.core.kernel.Input;
import com.pantheon.core.kernel.Window;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {

    private Vector3f position = new Vector3f(0,0,0);
    private float pitch = 0f, yaw = 0f, roll = 0f;

    public Camera() {
    }

    public void move() {
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_W)) {
            this.position.z -= 0.02f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_D)) {
            this.position.x += 0.02f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_A)) {
            this.position.x -= 0.02f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_S)) {
            this.position.z += 0.02f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_UP)) {
            this.position.y += 0.02f;
        }
        if (Input.getInstance().isKeyHolding(GLFW.GLFW_KEY_DOWN)) {
            this.position.y -= 0.02f;
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
