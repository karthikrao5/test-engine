package com.pantheon.core.shaders;

import com.pantheon.core.buffers.BufferModel;
import com.pantheon.core.models.Model;
import com.pantheon.core.models.Texture;
import com.pantheon.core.utils.ResourceLoader;

public class Triangle extends ShaderProgram {
    private BufferModel buffer;

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
        Texture texture = ResourceLoader.importTextureFile("wall_mid.png");
        texture.setTextCoords(textCoords);
        model.setTexture(texture);

        buffer = new BufferModel(model);
    }

    public void render() {
        this.start();
        buffer.draw();
        this.stop();
    }

    public void cleanUp() {
        buffer.cleanUp();
    }

    @Override
    protected void bindAttributes() {
        this.bindAttribute(0, "position");
        this.bindAttribute(1, "textureCoords");
    }
}
