package com.pantheon.core.kernel;

import com.pantheon.core.buffers.BufferModel;
import com.pantheon.core.camera.Camera;
import com.pantheon.core.models.*;
import com.pantheon.core.utils.OBJLoader;
import com.pantheon.core.utils.ResourceLoader;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

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
    private double angle;

    public RenderEngine() {
        window = Window.getInstance();
        masterRenderer = new MasterRenderer();

        List<String> colors = Arrays.asList("blue", "black", "white");
        Random rand = new Random();

        RawModel rawModel;
//        try {
//            rawModel = OBJLoader.loadObj("/objs/dragon.obj");
//            TexturedModel texturedModel = new TexturedModel(
//                    ResourceLoader.importTextureFile("blue.png"), rawModel);
//            texturedModel.setReflectivity(0.5f);
//            texturedModel.setShineDamper(10);
//
////            for (int i = 0; i < 100; i++) {
////                float x = rand.nextFloat() * 800 - 50;
////                float y = 0;
////                float z = rand.nextFloat() * -300;
////
////                Entity entity = new Entity(texturedModel,
////                        new Vector3f(x, y, z),0f,0f, 0f, 1f);
////
////                masterRenderer.processEntity(entity);
////            }
//
//            Entity entity = new Entity(texturedModel,
//                    new Vector3f(0, 0, 0),0f,0f, 0f, 1f);
//
//            masterRenderer.processEntity(entity);
//        } catch (IOException e) {
//            System.exit(1);
//            e.printStackTrace();
//        }

//        try {
//            rawModel = OBJLoader.loadObj("/objs/grassModel.obj");
//            TexturedModel texturedModel = new TexturedModel(
//                    ResourceLoader.importTextureFile("grassTexture.png"), rawModel);
//            texturedModel.setReflectivity(0.5f);
//            texturedModel.setShineDamper(10);
//
//            for (int i = 0; i < 300; i++) {
//                float x = rand.nextFloat() * 800 - 50;
//                float y = 0;
//                float z = rand.nextFloat() * -300;
//
//                Entity entity = new Entity(texturedModel,
//                        new Vector3f(x, y, z),0f,0f, 0f, 1f);
//
//                masterRenderer.processEntity(entity);
//            }
//
//        } catch (IOException e) {
//            System.exit(1);
//            e.printStackTrace();
//        }

        int terrainTextureId = ResourceLoader.importTextureFile("grass.png");
        System.out.println("Texture id in render engine: " + terrainTextureId);
//        Terrain terrain = new Terrain(0,0);
//        TexturedModel texturedTerrain = new TexturedModel(terrainTextureId, terrain.getModel());
//        texturedTerrain.setShineDamper(10f);
//        texturedTerrain.setReflectivity(0.1f);
//        texturedTerrain.setHeightScale(20f);
//        terrain.setTexturedModel(texturedTerrain);
//
        Terrain terrain2 = new Terrain(0, 0);
        terrain2.generateTerrain();
        TexturedModel texturedTerrain2 = new TexturedModel(terrainTextureId, terrain2.getModel());
        texturedTerrain2.setShineDamper(10f);
        texturedTerrain2.setReflectivity(0.1f);
        texturedTerrain2.setHeightScale(20f);
        terrain2.setTexturedModel(texturedTerrain2);
//
//        Terrain terrain3 = new Terrain(-1, -1);
//        TexturedModel texturedTerrain3 = new TexturedModel(terrainTextureId, terrain3.getModel());
//        texturedTerrain3.setShineDamper(10f);
//        texturedTerrain3.setReflectivity(0.1f);
//        texturedTerrain3.setHeightScale(20f);
//        terrain3.setTexturedModel(texturedTerrain3);
//
//        Terrain terrain4 = new Terrain(0, -1);
//        TexturedModel texturedTerrain4 = new TexturedModel(terrainTextureId, terrain4.getModel());
//        texturedTerrain4.setShineDamper(10f);
//        texturedTerrain4.setReflectivity(0.1f);
//        texturedTerrain4.setHeightScale(20f);
//        terrain4.setTexturedModel(texturedTerrain4);
//
//        masterRenderer.processTerrain(terrain);
        masterRenderer.processTerrain(terrain2);
//        masterRenderer.processTerrain(terrain3);
//        masterRenderer.processTerrain(terrain4);

        float[] vertices = {
                -0.5f, 1.0f, 0.5f,
                -0.5f, 1.0f, -0.5f,
                0.5f, 1.0f, -0.5f,
                0.5f, 1.0f, 0.5f,
        };

        int[] triangles = {
                0,1,3,
                3,1,2
        };

        float[] textures = {
                0,0,
                0,1,
                1,1,
                1,0
        };

        RawModel rawImage = new RawModel(vertices, triangles, textures, new float[1]);

        Image perlinImage = new Image(rawImage);
        masterRenderer.processImage(perlinImage);

        camera = new Camera(new Vector3f(0f, 100f, 0f));
        light = new Light(new Vector3f(100f, 500f, 100f), new Vector3f(1, 1, 1));
        angle = 0.0;
    }

    public void render() {
        updateLight();
        masterRenderer.render(light, camera);

        //swap buffers
        window.render();
    }

    public void updateLight() {
        if (angle >= 360.0) {
            angle = 0.0;
        }
        light.setPosition(new Vector3f(
                light.getPosition().x -= 5f* (float) Math.cos(Math.toRadians(angle)),
                light.getPosition().y,
                light.getPosition().z -= 5f* (float) Math.sin(Math.toRadians(angle))));

        angle += 1.0;
    }

    public void cleanUp() {
        masterRenderer.cleanUp();
    }

    public void update() {
        camera.move();
        masterRenderer.update();
        if (Input.getInstance().isKeyPushed(GLFW.GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(window.getWindowId(), true); // We will detect this in the rendering loop
        }
    }
}
