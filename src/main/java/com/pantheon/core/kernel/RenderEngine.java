package com.pantheon.core.kernel;

import com.pantheon.core.camera.Camera;
import com.pantheon.core.models.Entity;
import com.pantheon.core.models.RawModel;
import com.pantheon.core.models.TexturedModel;
import com.pantheon.core.utils.OBJLoader;
import com.pantheon.core.utils.ResourceLoader;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class RenderEngine {
    private Window window;
    private Camera camera;
    private Light light;
    private MasterRenderer masterRenderer;

    public RenderEngine() {
        window = Window.getInstance();
        masterRenderer = new MasterRenderer();

        List<String> colors = Arrays.asList("blue", "black", "white");
        Random rand = new Random();

        RawModel rawModel;
        try {
            rawModel = OBJLoader.loadObj("/objs/dragon.obj");
            TexturedModel texturedModel = new TexturedModel(
                    ResourceLoader.importTextureFile("blue.png"), rawModel);
            texturedModel.setReflectivity(0.5f);
            texturedModel.setShineDamper(10);

            for (int i = 0; i < 200; i++) {
                float x = rand.nextFloat() * 100 - 50;
                float y = rand.nextFloat() * 100 - 50;
                float z = rand.nextFloat() * -300;

                Entity entity = new Entity(texturedModel,
                        new Vector3f(x, y, z),
                        rand.nextFloat() * 180f,
                        rand.nextFloat() * 180f,
                        0f, 1f);

                masterRenderer.processEntity(entity);
            }
        } catch (IOException e) {
            System.exit(1);
            e.printStackTrace();
        }

        camera = new Camera();
        light = new Light(new Vector3f(0f, 0f, 0f), new Vector3f(1, 1, 1));
    }

    public void render() {
        masterRenderer.render(light, camera);

        //swap buffers
        window.render();
    }

    public void cleanUp() {
        masterRenderer.cleanUp();
    }

    public void update() {
        camera.move();

        if (Input.getInstance().isKeyPushed(GLFW.GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(window.getWindow(), true); // We will detect this in the rendering loop
        }
    }
}
