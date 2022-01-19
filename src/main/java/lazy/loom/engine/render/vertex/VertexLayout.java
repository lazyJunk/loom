package lazy.loom.engine.render.vertex;

import java.util.List;

public interface VertexLayout {

    List<VertexElement> getLayout();

    boolean hasColor();

    boolean hasUV();

    float[] toVertex();

    int size();
}
