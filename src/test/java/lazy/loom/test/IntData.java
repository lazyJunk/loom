package lazy.loom.test;

import lazy.loom.game.common.world.block.BlockData;

public class IntData {

    public static void main(String[] args) {
        BlockData data = new BlockData();
        data.id = 2;
        int compress = 120755104; /*ByteChunk.compress(new Vector3i(16, 15, 15), data);*/
        System.out.println(compress);
        //System.out.println("Vector: " + ByteChunk.extractPosition(compress).x + ", " + ByteChunk.extractPosition(compress).y + ", " + ByteChunk.extractPosition(compress).z);
        //.out.println("Data: " + ByteChunk.extractTexId(compress));
    }
}
