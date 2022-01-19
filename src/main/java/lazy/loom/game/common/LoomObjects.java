package lazy.loom.game.common;

import lazy.loom.game.common.utils.ContentLoader;
import lazy.loom.game.common.world.block.BlockData;
import lazy.loom.game.common.world.block.TextureData;

import java.util.List;
import java.util.Map;

import static lazy.loom.engine.util.Assert.tryCatchRet;

public class LoomObjects {
    public static Map<Integer, BlockData> blocks = tryCatchRet(ContentLoader::loadBlocks);
    public static Map<String, TextureData> textureData = tryCatchRet(ContentLoader::loadTextureData);
}
