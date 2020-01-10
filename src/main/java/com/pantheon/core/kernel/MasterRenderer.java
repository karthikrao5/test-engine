package com.pantheon.core.kernel;

import com.pantheon.core.camera.Camera;
import com.pantheon.core.models.Entity;
import com.pantheon.core.models.Image;
import com.pantheon.core.models.Terrain;
import com.pantheon.core.models.TexturedModel;
import com.pantheon.core.renderer.EntityRenderer;
import com.pantheon.core.renderer.ImageRenderer;
import com.pantheon.core.renderer.TerrainRenderer;
import com.pantheon.core.shaders.BaseShader;
import com.pantheon.core.shaders.ImageShader;
import com.pantheon.core.shaders.TerrainShader;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {
    private static final float FOV = 70f;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private Matrix4f projectionMatrix;

    private BaseShader entityShader;
    private TerrainShader terrainShader;
    private ImageShader imageShader;

    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;
    private ImageRenderer imageRenderer;

    private Map<TexturedModel, List<Entity>> entityMap;
    private List<Terrain> terrains;
    private List<Image> images;

    public MasterRenderer() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        createProjectionMatrix();

        this.entityShader = new BaseShader();
        this.terrainShader = new TerrainShader();
        this.imageShader = new ImageShader();

        this.entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
        this.terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        this.imageRenderer = new ImageRenderer(imageShader, projectionMatrix);

        this.entityMap = new HashMap<>();
        this.terrains = new ArrayList<>();
        this.images = new ArrayList<>();
    }

    public void render(Light light, Camera camera) {
        prepare();
        entityShader.start();
        entityShader.loadViewMatrix(camera);
        entityShader.loadLight(light);
        entityRenderer.render(entityMap);
        entityShader.stop();

        terrainShader.start();
        terrainShader.loadViewMatrix(camera);
        terrainShader.loadLight(light);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        imageShader.start();
        imageShader.loadViewMatrix(camera);
        imageRenderer.render(images);
        imageShader.stop();
    }

    public void update() {
        entityRenderer.update();
        terrainRenderer.update(terrains);
    }

    public void processImage(Image image) {
        images.add(image);
    }

    public void processEntity(Entity entity) {
        List<Entity> entityList = entityMap.get(entity.getTexturedModel());
        if (entityList != null) {
            entityList.add(entity);
        } else {
            List<Entity> entities = new ArrayList<>();
            entities.add(entity);
            entityMap.put(entity.getTexturedModel(), entities);
        }
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void cleanUp() {
        entityShader.cleanUp();
    }

    private void prepare() {
        glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glClearDepth(1.0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) Window.getInstance().getWidth() / (float) Window.getInstance().getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;

        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }
}
