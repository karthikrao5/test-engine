package com.pantheon.core.kernel;

import com.pantheon.core.buffers.BufferModel;
import com.pantheon.core.models.Entity;
import com.pantheon.core.models.Model;
import com.pantheon.core.renderer.Renderer;
import com.pantheon.core.shaders.BoxShader;
import com.pantheon.core.utils.ResourceLoader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class RenderEngine {
    private Window window;
    private BoxShader boxShader;
    private Entity entity;
    private Renderer renderer;

    public RenderEngine() {
        window = Window.getInstance();
        boxShader = new BoxShader();
        renderer = new Renderer(boxShader);

        float[] vertices = new float[]{
                -0.5f, 0.5f, 0.0f, //V0
                0.5f, 0.5f, 0.0f, //V2
                0.5f, -0.5f, 0.0f, //V2
                -0.5f,-0.5f, 0.0f, //V3
        };

        int[] triangles = new int[] {
                0,1,3,
                3,1,2
        };

        float[] textCoords = new float[] {
                1f,0f,
                0f,0f,
                0f,1f,
                1f,1f
        };

        Model model = new Model();
        model.setTriangles(triangles);
        model.setVertices(vertices);
        model.setTextureId(ResourceLoader.importTextureFile("wall_mid.png"));
        model.setTextCoords(textCoords);
        BufferModel bufferModel = new BufferModel(model);

        entity = new Entity(bufferModel, new Vector3f(0,0,-1), 0,0,0, 1);
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        entity.move(new Vector3f(0, 0, -0.1f));
        entity.rotate(new Vector3f(0,1,0));

        renderer.render(entity, boxShader);

        //swap buffers
        window.render();
    }

    public void cleanUp() {
        boxShader.cleanUp();
    }
}
