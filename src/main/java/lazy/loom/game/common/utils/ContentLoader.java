package lazy.loom.game.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lazy.loom.game.common.world.block.BlockData;
import lazy.loom.game.common.world.block.TextureData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentLoader {

    private static YAMLFactory factory = new YAMLFactory();
    private static ObjectMapper mapper = new ObjectMapper(factory);

    public static Map<Integer, BlockData> loadBlocks() throws IOException {
        Map<String, BlockData> loaded = mapper.readValue(new File("./assets/blocks.yaml"), new TypeReference<>() {});
        var blockMap = new HashMap<Integer, BlockData>();
        loaded.forEach((s, b) -> {
            blockMap.put((int)b.id, b);
        });
        return blockMap;
    }

    public static Map<String, TextureData> loadTextureData() throws IOException {
        return mapper.readValue(new File("./atlas.yaml"), new TypeReference<>() {});
    }
}
