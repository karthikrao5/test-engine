package com.pantheon.core.shaders;

import com.pantheon.core.utils.ResourceLoader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram {
    private int program;
    private int vertexId;
    private int fragId;
    private static FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexShaderFile, String fragShaderFile) {
        program = glCreateProgram();
        if (program == 0) {
            System.err.println("Unable to create shader program");
            System.exit(1);
        }

        bindAttributes();

        this.vertexId = addShader(ResourceLoader.loadShader(vertexShaderFile), GL_VERTEX_SHADER);
        this.fragId = addShader(ResourceLoader.loadShader(fragShaderFile), GL_FRAGMENT_SHADER);

        compileShader();
        getAllUniformLocations();
    }

    public void start() {
        glUseProgram(program);
    }

    public void stop() {
        glUseProgram(0);
    }

    protected abstract void bindAttributes();

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName) {
        return glGetUniformLocation(program, uniformName);
    }

    protected void bindAttribute(int attribute, String variableName) {
        glBindAttribLocation(program, attribute, variableName);
    }

    public void compileShader() {
        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
            System.out.println(this.getClass().getName() + " " + glGetProgramInfoLog(program, 1024));
            System.exit(1);
        }

        glValidateProgram(program);
        //TODO: figure out what this is for
//        if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0) {
//            System.err.println(this.getClass().getName() + " " + glGetProgramInfoLog(program, 1024));
//            System.exit(1);
//        }
    }

    protected void loadFloat(int location, float value) {
        glUniform1f(location, value);
    }

    protected void loadVector3f(int location, Vector3f vector) {
        glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        float[] arr = new float[16];
        arr[0]  = matrix.m00();
        arr[1]  = matrix.m01();
        arr[2]  = matrix.m02();
        arr[3]  = matrix.m03();
        arr[4]  = matrix.m10();
        arr[5]  = matrix.m11();
        arr[6]  = matrix.m12();
        arr[7]  = matrix.m13();
        arr[8]  = matrix.m20();
        arr[9]  = matrix.m21();
        arr[10] = matrix.m22();
        arr[11] = matrix.m23();
        arr[12] = matrix.m30();
        arr[13] = matrix.m31();
        arr[14] = matrix.m32();
        arr[15] = matrix.m33();
        matBuffer.put(arr).flip();
        glUniformMatrix4fv(location, false, matBuffer);
    }

    public void cleanUp() {
        stop();
        glDetachShader(program, vertexId);
        glDetachShader(program, fragId);

        glDeleteShader(vertexId);
        glDeleteShader(fragId);
        glDeleteProgram(program);
    }

    private int addShader(String code, int type) {
        int shader = glCreateShader(type);

        if (shader == 0) {
            System.err.println(this.getClass().getName() + " Shader creation failed");
            System.exit(1);
        }

        glShaderSource(shader, code);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
            System.err.println(this.getClass().getName() + " " + glGetShaderInfoLog(shader, 1024));
            System.exit(1);
        }

        glAttachShader(program, shader);
        return shader;
    }
}
