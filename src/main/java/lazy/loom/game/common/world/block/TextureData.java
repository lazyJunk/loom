package lazy.loom.game.common.world.block;

import java.util.Map;

public class TextureData {

    public int id;
    public String name;
    public Map<Integer, Vec2f> uvs;

    public record Vec2f(float x, float y) {}
}
