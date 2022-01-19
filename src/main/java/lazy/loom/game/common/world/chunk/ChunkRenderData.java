package lazy.loom.game.common.world.chunk;

import java.util.ArrayList;
import java.util.List;

public record ChunkRenderData(List<Vertex> vertexData, ArrayList<Integer> indices, int vertexCursor, int elements) {
}