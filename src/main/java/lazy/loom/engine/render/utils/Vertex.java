package lazy.loom.engine.render.utils;

import java.util.Arrays;
import java.util.List;

public record Vertex(float x, float y, float z, float u, float v) {
    public static int VERTEX_SIZE = 5 * Float.BYTES;

    public List<Float> asFloatArray() {
        return Arrays.asList(x, y, z, u, v);
    }
}
