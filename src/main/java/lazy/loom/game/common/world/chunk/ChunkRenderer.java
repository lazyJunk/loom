package lazy.loom.game.common.world.chunk;

import java.util.ArrayList;

import static lazy.loom.engine.render.GLU.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class ChunkRenderer {

    public int vao;
    public int[] indice;

    public void setup(IntChunk chunk) {
        var vertices = new ArrayList<Float>();

        for (int i = 0; i < chunk.renderData.vertexCursor(); i++) {
            Vertex vertex = chunk.renderData.vertexData().get(i);
            vertices.add(vertex.x);
            vertices.add(vertex.y);
            vertices.add(vertex.z);
            vertices.add(vertex.face);
            vertices.add(vertex.u);
            vertices.add(vertex.v);
        }

        /*

        for (var vertex : chunk.renderData.vertexData()) {
            if (vertex.position != null) {
                vertices.add(vertex.position.x);
                vertices.add(vertex.position.y);
                vertices.add(vertex.position.z);
                vertices.add((float)vertex.face);
                vertices.add(vertex.uv.x);
                vertices.add(vertex.uv.y);
            }
        }*/

        var foo = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            foo[i] = vertices.get(i);
        }

         indice = new int[chunk.renderData.elements()];
        for (int i = 0; i < chunk.renderData.elements(); i++) {
            indice[i] = chunk.renderData.indices().get(i);
        }

        vao = vao();
        int vbo = vbo(foo);
        int ibo = ibo(indice);
        vertexAttr(0, 3, 6 * Float.BYTES, 0); //Pos
        vertexAttr(1, 1, 6 * Float.BYTES, Float.BYTES); //Normal
        vertexAttr(2, 2, 6 * Float.BYTES, 4 * Float.BYTES); //UV
        glBindVertexArray(0);

        chunk.cleanData();
    }

    public void render(IntChunk chunk) {
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glDrawElements(GL_TRIANGLES, indice.length, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }
}
