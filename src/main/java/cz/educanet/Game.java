package cz.educanet;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import org.lwjgl.glfw.GLFW;

public class Game {

    private static float[] vertices = {
            0.5f, 0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
    };

    private static float speed = 0.01f;

    private static int triangleVaoId;
    private static int triangleVboId;

    public static void init() {
        Shaders.initShaders();

        triangleVaoId = GL33.glGenVertexArrays();
        triangleVboId = GL33.glGenBuffers();

        GL33.glBindVertexArray(triangleVaoId);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, triangleVboId);

        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);

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

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);
        MemoryUtil.memFree(fb);
    }

}
