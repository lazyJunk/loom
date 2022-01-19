package lazy.loom.game.common.world.chunk;

public class Vertex {

    public float x;
    public float y;
    public float z;
    public float u;
    public float v;
    public float face = 0;

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void posFrom(Vertex a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    public void uvFrom(float u, float v) {
        this.u = u;
        this.v = v;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", u=" + u +
                ", v=" + v +
                ", face=" + face +
                '}';
    }
}
