package lazy.loom.engine.render;

import static org.lwjgl.opengl.GL33.*;

public class GLU {

    public static int vao() {
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
        return vao;
    }

    public static int vbo(float[] vertexData) {
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        return vbo;
    }

    public static int ibo(int[] indices) {
        int ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        return ibo;
    }

    public static void vertexAttr(int index, int size, int vertexDataSize, int pointer) {
        glVertexAttribPointer(index, size, GL_FLOAT, false, vertexDataSize, pointer);
        glEnableVertexAttribArray(index);
    }

    public static void vertexAttrByte(int index, int size, int vertexDataSize, int pointer) {
        glVertexAttribIPointer(index, size, GL_UNSIGNED_BYTE, vertexDataSize, pointer);
        glEnableVertexAttribArray(index);
    }

    public static void vertexAttrShort(int index, int size, int vertexDataSize, int pointer) {
        glVertexAttribPointer(index, size, GL_SHORT, false, vertexDataSize, pointer);
        glEnableVertexAttribArray(index);
    }

    public static void vertexAttrInt(int index, int size, int vertexDataSize, int pointer) {
        glVertexAttribIPointer(index, size, GL_UNSIGNED_INT, vertexDataSize, pointer);
        glEnableVertexAttribArray(index);
    }

    public static void clearBuffers(int vao, int vbo, int ibo) {
        glDeleteBuffers(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);
    }

    public static void cullFace(int face) {
        glEnable(GL_CULL_FACE);
        glCullFace(face);
    }
}
