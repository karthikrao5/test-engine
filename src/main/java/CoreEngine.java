import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class CoreEngine {
    private ShaderProgram program;
    private int vboId;
    private int vaoId;
    private int fps;
    private float framerate = 200;
    private float frameTime = 1.0f / framerate;
    private boolean isRunning;

    public void createWindow(int width, int height) {
        System.out.println("LWJGL version: " + Version.getVersion() + "!");

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        Window.getInstance().create(width, height);
    }

    public void start() {
        if (isRunning) {
            return;
        }
        run();
    }

    private void run() {
        this.isRunning = true;

        int frames = 0;
        long frameCounter = 0;

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        this.program = new ShaderProgram();

        int vertexShaderId = program.addVertexShader(ResourceLoader.loadShader("shaders/vertex.glsl"));
        int fragShaderId = program.addFragShader(ResourceLoader.loadShader("shaders/frag.glsl"));

        program.compileShader();

        glDeleteShader(vertexShaderId);
        glDeleteShader(fragShaderId);

        final float[] vertices = new float[]{
                -0.5f, 0.0f, 0.0f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f
        };

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        FloatBuffer verticesBuff = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuff.put(vertices).flip();

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        glBufferData(GL_ARRAY_BUFFER, verticesBuff, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false,  0, 0);
        glEnableVertexAttribArray(0);

        // Rendering Loop
        while (isRunning) {
            boolean render = false;

            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) Constants.NANOSECOND;
            frameCounter += passedTime;


            while (unprocessedTime > frameTime) {

                render = true;
                unprocessedTime -= frameTime;

                if (Window.getInstance().isCloseRequested())
                    stop();

                update();

                if (frameCounter >= Constants.NANOSECOND) {
                    setFps(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }
            if (render) {
                render();
                frames++;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        cleanUp();
    }

    private void cleanUp() {
        Window.getInstance().destroy();
    }

    private void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
    }

    private void update() {
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        glUseProgram(program.getProgramId());

        glBindVertexArray(vaoId);

        glDrawArrays(GL_TRIANGLES, 0, 3);

        glUseProgram(0);

        glfwSwapBuffers(Window.getInstance().getWindowId()); // swap the color buffers

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void init() {

    }
}
