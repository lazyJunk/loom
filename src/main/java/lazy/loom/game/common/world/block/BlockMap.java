package lazy.loom.game.common.world.block;

import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockMap {

    public Map<String, Integer> nameToId = new HashMap<>();
    public List<BlockData> blockData = new ArrayList<>();
    public Map<String, TextureData> textureData = new HashMap<>();

    public record BlockData(String side, String top, String bottom) {}
    public record TextureData(String textureName, Vector4f[] uvs) {}
}
