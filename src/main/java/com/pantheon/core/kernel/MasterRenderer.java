package com.pantheon.core.kernel;

import com.pantheon.core.camera.Camera;
import com.pantheon.core.models.Entity;
import com.pantheon.core.models.TexturedModel;
import com.pantheon.core.renderer.Renderer;
import com.pantheon.core.shaders.BaseShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    private BaseShader shader;
    private Renderer renderer;
    private Map<TexturedModel, List<Entity>> entityMap;


    public MasterRenderer() {
        this.shader = new BaseShader();
        this.renderer = new Renderer(shader);
        this.entityMap = new HashMap<>();
    }

    public void render(Light light, Camera camera) {
        renderer.prepare();
        shader.start();

        shader.loadViewMatrix(camera);
        shader.loadLight(light);

        renderer.render(entityMap);

        shader.stop();
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

    public void cleanUp() {
        shader.cleanUp();
    }
}
