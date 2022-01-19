package lazy.loom.game.common.world.block;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lazy.loom.game.common.world.block.BlockData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BlockGenerator {

    private static Map<String, BlockData> dataMap = new HashMap<>();

    private static YAMLFactory factory = new YAMLFactory();
    private static ObjectMapper mapper = new ObjectMapper(factory);

    public static void createBlock() {

        BlockData data = new BlockData();
        data.id = 0;
        data.name = "grass";
        data.bottom = "dirt";
        data.top = "grass_top";
        data.side = "grass";

        BlockData data1 = new BlockData();
        data1.id = 1;
        data1.name = "dirt";
        data1.bottom = "dirt";
        data1.top = "dirt";
        data1.side = "dirt";

        dataMap.put("grass", data);
        dataMap.put("dirt", data1);

        try {
            mapper.writeValue(new File("./assets/blocks.yaml"), dataMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
