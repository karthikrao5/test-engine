package com.pantheon.core.shaders;

import com.pantheon.core.buffers.BufferModel;
import com.pantheon.core.models.Entity;
import com.pantheon.core.models.Model;
import com.pantheon.core.models.Texture;
import com.pantheon.core.utils.ResourceLoader;
import org.joml.Vector3f;

public class Triangle extends ShaderProgram {
    private Entity entity;
    private int location_transformationMatrix;

    public Triangle() {
        super("vertex.glsl", "frag.glsl");

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
        BufferModel buffer = new BufferModel(model);

        entity = new Entity(buffer, new Vector3f(0,0,0), 0,0,0,1);
    }

    public void render() {
        this.start();
        entity.getModel().draw();
        this.stop();
    }

    public void cleanUp() {
        entity.getModel().cleanUp();
    }

    @Override
    protected void bindAttributes() {
        this.bindAttribute(0, "position");
        this.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }
}
