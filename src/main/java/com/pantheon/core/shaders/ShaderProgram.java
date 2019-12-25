package com.pantheon.core.shaders;

import com.pantheon.core.utils.ResourceLoader;

import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram {
    private int program;
    private int vertexId;
    private int fragId;

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
    }

    public void start() {
        glUseProgram(program);
    }

    public void stop() {
        glUseProgram(0);
    }

    protected abstract void bindAttributes();

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
