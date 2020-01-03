package com.pantheon.core.utils;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.*;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class ResourceLoader {

    public static String loadFileAsString(String fileName) {
        StringBuilder fileAsString = new StringBuilder();
        BufferedReader bufferedReader;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ResourceLoader.class.getResourceAsStream(fileName)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileAsString.append(line).append("\n");
            }

            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return fileAsString.toString();
    }

    public static int importTextureFile(String fileName) {
        int textureId = 0;
        try {
            PNGDecoder decoder = new PNGDecoder(ResourceLoader.class.getResourceAsStream("/textures/" + fileName));
            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);

            //flip the buffer so its ready to read
            buffer.flip();

            glEnable(GL_TEXTURE_2D);

            //create a texture
            textureId = glGenTextures();

            //bind the texture
            glBindTexture(GL_TEXTURE_2D, textureId);

            //tell opengl how to unpack bytes
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            //set the texture parameters, can be GL_LINEAR or GL_NEAREST

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            //upload texture
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            // Generate Mip Map
            glGenerateMipmap(GL_TEXTURE_2D);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.printf("Setting textureId: %d\n", textureId);
        return textureId;
    }
}