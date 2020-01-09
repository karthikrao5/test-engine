package com.pantheon.core.kernel;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private static Input _input;

    private ArrayList<Integer> pushedKeys = new ArrayList<>();
    private ArrayList<Integer> keysHolding = new ArrayList<>();
    private ArrayList<Integer> releasedKeys = new ArrayList<>();

    private ArrayList<Integer> pushedButtons = new ArrayList<>();
    private ArrayList<Integer> buttonsHolding = new ArrayList<>();
    private ArrayList<Integer> releasedButtons = new ArrayList<>();

    @SuppressWarnings("unused")
    private GLFWKeyCallback keyCallback;

    private final Vector2d previousPos;
    private final Vector2d currentPos;
    private final Vector2f displVec;
    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;


    public static Input getInstance() {
        if (_input == null) {
            _input = new Input();
        }
        return _input;
    }

    public Input() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    public void init(Window window) {
        glfwSetKeyCallback(window.getWindowId(), (keyCallback) = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW_PRESS) {
                    if (!pushedKeys.contains(key)) {
                        pushedKeys.add(key);
                        keysHolding.add(key);
                    }
                }

                if (action == GLFW_RELEASE) {
                    keysHolding.remove(Integer.valueOf(key));
                    releasedKeys.add(key);
                }
            }
        });

        glfwSetCursorPosCallback(window.getWindowId(), (windowHandle, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });
        glfwSetCursorEnterCallback(window.getWindowId(), (windowHandle, entered) -> {
            inWindow = entered;
        });
        glfwSetMouseButtonCallback(window.getWindowId(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    public void input() {
        displVec.x = 0;
        displVec.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public boolean isKeyPushed(int key) {
        return pushedKeys.contains(key);
    }
    public boolean isKeyHolding(int key) {
        return keysHolding.contains(key);
    }

    public void update() {
        pushedKeys.clear();
        releasedKeys.clear();
        pushedButtons.clear();
        releasedButtons.clear();

        glfwPollEvents();
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
