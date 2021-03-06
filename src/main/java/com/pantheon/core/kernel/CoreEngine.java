package com.pantheon.core.kernel;

import com.pantheon.core.utils.Constants;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.opengl.GL11.glClearColor;

public class CoreEngine {
    private int fps;
    private float framerate = 200;
    private float frameTime = 1.0f / framerate;
    private boolean isRunning;
    private RenderEngine renderEngine;
    private Input input;

    public void createWindow(int width, int height) {
        System.out.println("LWJGL version: " + Version.getVersion() + "!");

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        Window.getInstance().create(width, height);

        renderEngine = new RenderEngine();
    }

    public void start() {
        if (isRunning) {
            return;
        }
        run();
    }

    private void run() {
        this.isRunning = true;

        int frames = 0;
        long frameCounter = 0;

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Rendering Loop
        while (isRunning) {
            boolean render = false;

            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) Constants.NANOSECOND;
            frameCounter += passedTime;


            while (unprocessedTime > frameTime) {

                render = true;
                unprocessedTime -= frameTime;

                if (Window.getInstance().isCloseRequested())
                    stop();

                update();

                if (frameCounter >= Constants.NANOSECOND) {
                    setFps(frames);
                    Window.getInstance().setWindowTitle(String.format("%d frames",frames));
                    frames = 0;
                    frameCounter = 0;
                }
            }
            if (render) {
                render();
                frames++;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        cleanUp();
    }

    private void cleanUp() {
        renderEngine.cleanUp();
        Window.getInstance().cleanUp();
    }

    private void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
    }

    private void update() {
        Input.getInstance().input();
        Input.getInstance().update();
        renderEngine.update();
    }

    private void render() {
        renderEngine.render();
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void init() {
        Input.getInstance().init(Window.getInstance());
    }
}
