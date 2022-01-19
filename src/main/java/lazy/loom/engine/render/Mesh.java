package lazy.loom.engine.render;

import lazy.loom.engine.render.utils.Vertex;

import java.util.ArrayList;
import java.util.List;

import static lazy.loom.engine.render.GLU.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Mesh {

    private int vao = 0;
    private int vbo = 0;
    private int ibo = 0;

    private List<Float> tmp = new ArrayList<>();

    private float[] vertices = new float[]{};
    private int[] indices = new int[]{};

    public Mesh(float[] vertices, int[] indices) {
        super();
        this.vertices = vertices;
        this.indices = indices;
        vao = vao();
        vbo = vbo(vertices);
        ibo = ibo(indices);
        vertexAttr(0, 3, Vertex.VERTEX_SIZE, 0);
        vertexAttr(1, 3, Vertex.VERTEX_SIZE, 3 * Float.BYTES);
    }

    public Mesh() {
    }

    public static Mesh triangle(Vertex v1, Vertex v2, Vertex v3) {
        return new Mesh(new float[]{v1.x(), v1.y(), v1.z(), v1.u(), v1.v(), v2.x(), v2.y(), v2.z(), v2.u(), v2.v(), v3.x(), v3.y(), v3.z(), v3.u(), v3.v()}, new int[]{0, 1, 2});
    }

    public static Mesh quad(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        return new Mesh(new float[]{v1.x(), v1.y(), v1.z(), v1.u(), v1.v(), v2.x(), v2.y(), v2.z(), v2.u(), v2.v(), v3.x(), v3.y(), v3.z(), v3.u(), v3.v(), v4.x(), v4.y(), v4.z(), v4.u(), v4.v()}, new int[]{0, 1, 3, 1, 2, 3});
    }

    public static Mesh createBuilder() {
        return new Mesh();
    }

    public Mesh addQuads(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        tmp.addAll(v1.asFloatArray());
        tmp.addAll(v2.asFloatArray());
        tmp.addAll(v3.asFloatArray());
        tmp.addAll(v4.asFloatArray());
        return this;
    }

    public void build(int[] indices) {
        float[] tmpVertex = new float[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            tmpVertex[i] = tmp.get(i);
        }
        this.indices = indices;
        vao = vao();
        vbo = vbo(tmpVertex);
        ibo = ibo(indices);
        vertexAttr(0, 3, Vertex.VERTEX_SIZE, 0);
        vertexAttr(1, 3, Vertex.VERTEX_SIZE, 3 * Float.BYTES);
    }

    public void render() {
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
    }
}
