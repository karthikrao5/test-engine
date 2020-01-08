package com.pantheon.core.kernel;

import com.pantheon.core.camera.Camera;
import com.pantheon.core.models.Entity;
import com.pantheon.core.models.RawModel;
import com.pantheon.core.models.TexturedModel;
import com.pantheon.core.renderer.Renderer;
import com.pantheon.core.shaders.BaseShader;
import com.pantheon.core.utils.OBJLoader;
import com.pantheon.core.utils.ResourceLoader;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

public class RenderEngine {
    private Window window;
    private BaseShader boxShader;
    private Entity entity;
    private Renderer renderer;
    private Camera camera;
    private Light light;
    private List<Entity> entities;

    public RenderEngine() {
        window = Window.getInstance();
        boxShader = new BaseShader();
        renderer = new Renderer(boxShader);

        entities = new ArrayList<>();
        List<String> colors = Arrays.asList("blue", "black", "white");
        Random rand = new Random();

        RawModel rawModel = null;
        try {
            rawModel = OBJLoader.loadObj("/objs/dragon.obj");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        TexturedModel texturedModel = new TexturedModel(ResourceLoader.importTextureFile(String.format("%s.png", colors.get(rand.nextInt(3)))), rawModel);
        texturedModel.setReflectivity(0.5f);
        texturedModel.setShineDamper(10);


        for (int i = 0; i < 200; i++) {
            float x = rand.nextFloat() * 100 - 50;
            float y = rand.nextFloat() * 100 - 50;
            float z = rand.nextFloat() * -300;

            entity = new Entity(texturedModel, new Vector3f(x, y, z), rand.nextFloat() * 180f, rand.nextFloat() * 180f, 0f, 1f);
            entities.add(entity);
        }

        camera = new Camera();
        light = new Light(new Vector3f(0, 10f, 10f), new Vector3f(1, 1, 1));
    }

    public void render() {
        glClearColor(0.2f, 0.2f, 0.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glClearDepth(1.0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        boxShader.start();

        boxShader.loadViewMatrix(camera);
        boxShader.loadLight(light);

        boxShader.loadShineVariables(entity.getTexturedModel().getShineDamper()
                , entity.getTexturedModel().getReflectivity());


        for (Entity entity : entities) {
            entity.rotate(new Vector3f(0, -0.2f, 0));
            renderer.render(entity, boxShader);
        }

        boxShader.stop();
        //swap buffers
        window.render();
    }

    public void cleanUp() {
        entity.getTexturedModel().cleanUp();
        boxShader.cleanUp();
    }

    public void update() {
        camera.move();

        if (Input.getInstance().isKeyPushed(GLFW.GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(window.getWindow(), true); // We will detect this in the rendering loop
        }
    }
}
