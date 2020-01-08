package com.pantheon.core.utils;

import com.pantheon.core.models.RawModel;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class OBJLoader {
    public static RawModel loadObj(String filename) throws FileNotFoundException {
        if (!filename.contains("/objs/")) {
            throw new FileNotFoundException();
        }

        String file = ResourceLoader.loadFileAsString(filename);
        String[] lines = file.split("\n");

        Pattern vertexPattern = Pattern.compile("^(vn|v|vt)\\s*?(-?\\d*\\.\\d*)\\s(-?\\d*\\.\\d*)\\s(-?\\d*\\.\\d*)");
        Pattern facePattern = Pattern.compile("^(f)\\s(\\d*\\/\\d*\\/\\d*)\\s(\\d*\\/\\d*\\/\\d*)\\s(\\d*\\/\\d*\\/\\d*)");

        List<Vector3f> vertexList = new ArrayList<>();
        List<Vector2f> textureCoordsList = new ArrayList<>();
        List<Vector3f> normalsList = new ArrayList<>();

        int[] triangles = null;

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith("v ")
                    || lines[i].startsWith("vt ")
                    || lines[i].startsWith("vn ")) {

                String[] found = lines[i].split(" ");

                if (lines[i].startsWith("v ")) {
                    vertexList.add(new Vector3f(
                            Float.parseFloat(found[1]),
                            Float.parseFloat(found[2]),
                            Float.parseFloat(found[3]))
                    );
                } else if (lines[i].startsWith("vt ")) {
                    textureCoordsList.add(new Vector2f(
                            Float.parseFloat(found[1]),
                            Float.parseFloat(found[2])
                    ));
                } else if (lines[i].startsWith("vn ")) {
                    normalsList.add(new Vector3f(
                            Float.parseFloat(found[1]),
                            Float.parseFloat(found[2]),
                            Float.parseFloat(found[3])
                    ));
                }
            }
        }

        System.out.println("VertexList Size: " + vertexList.size());
        System.out.println("TextCoords size: " + textureCoordsList.size());
        System.out.println("NormalsList size: " + normalsList.size());

        float[] normals = new float[vertexList.size() * 3];
        float[] textCoords = new float[vertexList.size() * 2];
        List<Integer> indices = new ArrayList<>();

        for (int j = 0; j < lines.length; j++) {
            if (lines[j].startsWith("f ")) {
                String[] vertexes = lines[j].split(" ");

                String[] vertex1 = vertexes[1].split("/");
                String[] vertex2 = vertexes[2].split("/");
                String[] vertex3 = vertexes[3].split("/");

                processVertex(vertex1, indices, textureCoordsList, normalsList, textCoords, normals);
                processVertex(vertex2, indices, textureCoordsList, normalsList, textCoords, normals);
                processVertex(vertex3, indices, textureCoordsList, normalsList, textCoords, normals);
            }
        }

        triangles = new int[indices.size()];
        float[] verticesArray = new float[vertexList.size() * 3];
        int pointer = 0;
        for (Vector3f vec : vertexList) {
            verticesArray[pointer++] = vec.x;
            verticesArray[pointer++] = vec.y;
            verticesArray[pointer++] = vec.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            triangles[i] = indices.get(i);
        }

        return new RawModel(verticesArray, triangles, textCoords, normals);
    }

    private static void processVertex(
            String[] vertexData,
            List<Integer> indices,
            List<Vector2f> texCoordsList,
            List<Vector3f> normalsList,
            float[] texArray,
            float[] normalsArray) {

        int vertexArrayPos = Integer.parseInt(vertexData[0]) -1;
        indices.add(vertexArrayPos);

        Vector2f currentTex = texCoordsList.get(Integer.parseInt(vertexData[1]) - 1);
        texArray[vertexArrayPos * 2] = currentTex.x;
        texArray[vertexArrayPos * 2 + 1] = 1 - currentTex.y;

        Vector3f currentNormal = normalsList.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[vertexArrayPos * 3] = currentNormal.x;
        normalsArray[vertexArrayPos * 3 + 1] = currentNormal.y;
        normalsArray[vertexArrayPos * 3 + 2] = currentNormal.z;
    }
}
