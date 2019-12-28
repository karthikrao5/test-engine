package com.pantheon.core.kernel;

import com.pantheon.core.buffers.BufferModel;
import com.pantheon.core.camera.Camera;
import com.pantheon.core.models.Entity;
import com.pantheon.core.models.RawModel;
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

    public RenderEngine() {
        window = Window.getInstance();
        boxShader = new BoxShader();
        renderer = new Renderer(boxShader);

        float[] vertices = new float[]{
                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,0.5f,-0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,-0.5f,0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                0.5f,0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                -0.5f,-0.5f,0.5f,
                -0.5f,0.5f,0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,0.5f,-0.5f,
                0.5f,0.5f,-0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,-0.5f,0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f
        };

        int[] triangles = new int[] {
                0,1,3,
                3,1,2,
                4,5,7,
                7,5,6,
                8,9,11,
                11,9,10,
                12,13,15,
                15,13,14,
                16,17,19,
                19,17,18,
                20,21,23,
                23,21,22
        };

        float[] textCoords = new float[] {
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0
        };

        RawModel rawModel = null;
        try {
            rawModel = OBJLoader.loadObj("/objs/dragon.obj");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        rawModel.setTextureId(ResourceLoader.importTextureFile("white.png"));
        BufferModel bufferModel = new BufferModel(rawModel);

        entity = new Entity(bufferModel, new Vector3f(0,-1f,-1f), 0,0,0, 0.1f);
        camera = new Camera();
    }

    public void render() {
        glClearColor(0.0f,0.0f,0.0f,1.0f);
        glEnable(GL_DEPTH_TEST);
        glClearDepth(1.0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        boxShader.start();

        boxShader.loadViewMatrix(camera);

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
