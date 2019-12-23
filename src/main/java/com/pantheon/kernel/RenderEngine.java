package com.pantheon.kernel;

import com.pantheon.shaders.Triangle;

import static org.lwjgl.opengl.GL11.*;

public class RenderEngine {
    private Window window;
    private Triangle triangle;

    public RenderEngine() {
        window = Window.getInstance();
        triangle = new Triangle();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        triangle.draw();

        //swap buffers
        window.render();
    }
}
