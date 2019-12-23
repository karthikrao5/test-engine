import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Main {
    private Window window;
    private int program;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        this.window = Window.getInstance();

        init();
        loop();

        window.destroy();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        window.create(1280, 720);
    }

    private void loop() {
        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        program = glCreateProgram();
        if (program == 0) {
            System.err.println("Unable to create shader program");
            System.exit(1);
        }

        int vertexShaderId = addShader(ResourceLoader.loadShader("shaders/vertex.glsl"), GL_VERTEX_SHADER);
        int fragShaderId = addShader(ResourceLoader.loadShader("shaders/frag.glsl"), GL_FRAGMENT_SHADER);

        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(program, 1024));
            System.exit(1);
        }

        glValidateProgram(program);

        if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0) {
            System.out.println(glGetShaderInfoLog(program, 1024));
        }

        glDeleteShader(vertexShaderId);
        glDeleteShader(fragShaderId);

        final float[] vertices = new float[]{
                -0.5f, 0.0f, 0.0f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f
        };

        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        FloatBuffer verticesBuff = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuff.put(vertices).flip();

        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        glBufferData(GL_ARRAY_BUFFER, verticesBuff, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false,  0, 0);
        glEnableVertexAttribArray(0);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window.getWindowId())) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

//            glBegin(GL_TRIANGLES);
//
//            // Top & Red
//            GL11.glColor3f(1.0f, 0.0f, 0.0f);
//            GL11.glVertex2f(0.0f, 0.5f);
//
//            // Right & Green
//            GL11.glColor3f(0.0f, 1.0f, 0.0f);
//            GL11.glVertex2f(0.5f, 0.5f);
//
//            // Left & Blue
//            GL11.glColor3f(0.0f, 0.0f, 1.0f);
//            GL11.glVertex2f(0.5f, -0.5f);
//
//            glEnd();
            glUseProgram(program);

            glBindVertexArray(vaoId);

            glDrawArrays(GL_TRIANGLES, 0, 3);

            glUseProgram(0);

            glfwSwapBuffers(window.getWindowId()); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    private int addShader(String code, int type) {
        int shaderId = glCreateShader(type);

        if (shaderId == 0) {
            System.err.println(this.getClass().getName() + " Shader creation failed");
            System.exit(1);
        }

        glShaderSource(shaderId, code);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            System.err.println(this.getClass().getName() + " " + glGetShaderInfoLog(shaderId, 1024));
            System.exit(1);
        }

        System.out.println("attaching shaderid: " + shaderId + "to progrma: " + program);
        glAttachShader(program, shaderId);
        return shaderId;
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
