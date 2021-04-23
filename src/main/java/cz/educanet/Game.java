package cz.educanet;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import org.lwjgl.glfw.GLFW;

public class Game {

    private static float[] vertices = {
            0.5f, 0.5f, 0.0f, // 0 -> Top right
            0.5f, -0.5f, 0.0f, // 1 -> Bottom right
            -0.5f, -0.5f, 0.0f, // 2 -> Bottom left
            -0.5f, 0.5f, 0.0f, // 3 -> Top left
            -0.5f, -0.5f, 0.0f, // 2 -> Bottom left
            0.5f, 0.5f, 0.0f, // 0 -> Top right
    };

    private static float speed = 0.01f;

    private static int triangleVaoId;
    private static int triangleVboId;

    public static void init() {
        // Setup shaders
        Shaders.initShaders();

        // Generate all the ids
        triangleVaoId = GL33.glGenVertexArrays();
        triangleVboId = GL33.glGenBuffers();

        // Tell OpenGL we are currently using this object (vaoId)
        GL33.glBindVertexArray(triangleVaoId);
        // Tell OpenGL we are currently writing to this buffer (vboId)
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, triangleVboId);

        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        // Send the buffer (positions) to the GPU
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);

        // Clear the buffer from the memory (it's saved now on the GPU, no need for it here)
        MemoryUtil.memFree(fb);
    }

    public static void render() {
        GL33.glUseProgram(Shaders.shaderProgramId);
        GL33.glBindVertexArray(triangleVaoId);
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, vertices.length / 3);
    }

    public static void update(long window) {

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            move(0, 1);
            sendToGPU();
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            move(0, -1);
            sendToGPU();
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            move(1, 1);
            sendToGPU();
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            move(1, -1);
            sendToGPU();
        }
    }

    public static void move(int axis, int operation) {
        for (int x = 0; x < vertices.length; x++) {
            if (x % 3 == axis) {
                float tmpVal = vertices[x] + speed * operation;
                if (tmpVal <= 1 && tmpVal >= -1) {
                    vertices[x] += speed * operation;
                    System.out.println(vertices[x]);
                }
            }
        }
    }

    private static void sendToGPU(){
        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        // Send the buffer (positions) to the GPU
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);
        MemoryUtil.memFree(fb);
    }

}
