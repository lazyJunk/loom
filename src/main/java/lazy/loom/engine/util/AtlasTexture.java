package lazy.loom.engine.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lazy.loom.game.common.world.block.TextureData;
import lazy.loom.game.common.world.block.TextureData.Vec2f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.sqrt;
import static lazy.loom.engine.util.Assert.notNull;
import static lazy.loom.engine.util.FuncHelper.timeCall;
import static lazy.loom.engine.util.Log.log;

public class AtlasTexture {

    private static YAMLFactory factory = new YAMLFactory();
    private static ObjectMapper mapper = new ObjectMapper(factory);

    public static void createAtlas() throws Exception {

        long time = timeCall(() -> {

            Map<String, TextureData> textureMap = new HashMap<>();

            var files = notNull(Paths.get("./assets/blocks").toFile().listFiles());
            int maxSize = (int) sqrt(32f * 32f * files.length);
            var atlas = new BufferedImage(maxSize, maxSize, BufferedImage.TYPE_INT_ARGB);

            int x = 0;
            int y = 0;
            int index = 0;
            for (var it : files) {
                var image = ImageIO.read(it);
                if ((x + image.getWidth()) > maxSize) {
                    y += image.getHeight();
                    x = 0;
                }

                atlas.getGraphics().drawImage(image, x, y, null);

                float ms = (float) maxSize;

                var uv0 = new Vec2f((x + image.getWidth()) / ms, y / ms);
                var uv1 = new Vec2f((x + image.getWidth()) / ms, (y + image.getHeight()) / ms);
                var uv2 = new Vec2f(x / ms, (y + image.getHeight()) / ms);
                var uv3 = new Vec2f(x / ms, y / ms);

                Map<Integer, Vec2f> uvMap = new HashMap<>();
                uvMap.put(0, uv0);
                uvMap.put(1, uv1);
                uvMap.put(2, uv2);
                uvMap.put(3, uv3);

                TextureData textureData = new TextureData();
                textureData.id = index;
                textureData.name = it.getName().split(".png")[0];

                textureData.uvs = uvMap;

                textureMap.put(textureData.name, textureData);

                x += image.getWidth();
                index++;
            }

            mapper.writeValue(new File("./atlas.yaml"), textureMap);

            ImageIO.write(atlas, "PNG", new File("./atlas.png"));
        });

        log("Took %sms to create the atlas texture.", time);
    }
}
