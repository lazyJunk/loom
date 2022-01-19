package lazy.loom.engine.render.vertex;

import java.util.Arrays;
import java.util.List;

public record VertexColor(float x, float y, float z, float r, float g, float b, float a) implements VertexLayout {

    public static final int SIZE = 7 * Float.BYTES;

    @Override
    public List<VertexElement> getLayout() {
        return Arrays.asList(
                new VertexElement(0, 3, 0),
                new VertexElement(1, 4, 3 * Float.BYTES)
        );
    }

    @Override
    public boolean hasColor() {
        return true;
    }

    @Override
    public boolean hasUV() {
        return false;
    }

    @Override
    public float[] toVertex() {
        return new float[]{
                x, y, z, r, g, b, a
        };
    }

    @Override
    public int size() {
        return SIZE;
    }
}
