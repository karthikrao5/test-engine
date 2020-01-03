package com.pantheon.core.kernel;

import com.pantheon.core.buffers.BufferModel;
import com.pantheon.core.camera.Camera;
import com.pantheon.core.models.Entity;
import com.pantheon.core.models.RawModel;
import com.pantheon.core.models.Texture;
import com.pantheon.core.renderer.Renderer;
import com.pantheon.core.shaders.BoxShader;
import com.pantheon.core.utils.OBJLoader;
import com.pantheon.core.utils.ResourceLoader;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.io.FileNotFoundException;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

public class RenderEngine {
    private Window window;
    private BoxShader boxShader;
    private Entity entity;
    private Renderer renderer;
    private Camera camera;
    private Light light;

    public RenderEngine() {
        window = Window.getInstance();
        boxShader = new BoxShader();
        renderer = new Renderer(boxShader);

        RawModel rawModel = null;
        try {
            rawModel = OBJLoader.loadObj("/objs/dragon.obj");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Texture texture = new Texture(ResourceLoader.importTextureFile("white.png"));

        BufferModel bufferModel = new BufferModel(rawModel, texture);

        entity = new Entity(bufferModel, new Vector3f(0,0,-25f), 0,0,0, 1f);
        camera = new Camera();
        light = new Light(new Vector3f(0,0, 10f), new Vector3f(1,1,1));
    }

    public void render() {
        glClearColor(0.0f,0.0f,0.0f,1.0f);
        glEnable(GL_DEPTH_TEST);
        glClearDepth(1.0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        boxShader.start();

        boxShader.loadViewMatrix(camera);
        boxShader.loadLight(light);
//        entity.move(new Vector3f(0, 0, 0));
        entity.rotate(new Vector3f(0,-0.5f,0));

        renderer.render(entity, boxShader);

        boxShader.stop();
        //swap buffers
        window.render();
    }

    public void cleanUp() {
        entity.getBufferModel().cleanUp();
        boxShader.cleanUp();
    }

    public void update() {
        camera.move();

        if (Input.getInstance().isKeyPushed(GLFW.GLFW_KEY_ESCAPE)){
            glfwSetWindowShouldClose(window.getWindow(), true); // We will detect this in the rendering loop
        }
    }
}
