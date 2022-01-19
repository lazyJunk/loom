package lazy.loom.game.common.world.chunk;

import lazy.loom.engine.util.rdparty.FastNoiseLite;
import lazy.loom.game.common.LoomObjects;
import lazy.loom.game.common.world.block.BlockData;
import lazy.loom.game.common.world.block.TextureData;
import org.joml.Vector2i;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static lazy.loom.engine.util.Assert.tryCatch;
import static lazy.loom.engine.util.FuncHelper.timeCall;
import static lazy.loom.engine.util.Log.log;
import static lazy.loom.engine.util.math.MathHelper.mapRange;

public class IntChunk {

    public byte[] chunkData;
    public ChunkRenderData renderData;
    public Vector2i chunkPos;
    private ArrayList<Vertex> vertexData;
    private ArrayList<Integer> elements;

    public static final int CHUNK_WIDTH = 16;
    public static final int CHUNK_HEIGHT = 256;
    public static final int CHUNK_DEPTH = 16;

    public void generateChunk(int xIn, int zIn, int seed) {
        chunkPos = new Vector2i(xIn, zIn);
        int worldX = chunkPos.x * 16;
        int worldZ = chunkPos.y * 16;

        if (chunkData == null) {
            this.chunkData = new byte[CHUNK_WIDTH * CHUNK_HEIGHT * CHUNK_DEPTH];

            FastNoiseLite lite = new FastNoiseLite();
            lite.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
            lite.SetFrequency(0.001f);
            lite.SetSeed(seed);
            lite.SetFractalOctaves(3);

            Random rnd = new Random();

            for (int y = 0; y < CHUNK_HEIGHT; y++) {
                for (int x = 0; x < CHUNK_DEPTH; x++) {
                    for (int z = 0; z < CHUNK_WIDTH; z++) {
                        final int arrayExpansion = vec3toArrayIndex(x, y, z);
                        float maxHeight = mapRange(
                                (float) lite.GetNoise((x + worldX) + seed / 3f, (z + worldZ) + seed / 3f)
                                , -1f, 1f, 0f, 1f) * 256;
                        int intMax = (int) Math.ceil(maxHeight);
                        int i = rnd.nextInt(intMax - 5, intMax - 1);
                        int j = rnd.nextInt(15);

                        if (y < intMax) {
                            chunkData[arrayExpansion] = 3;
                        } else if (y == intMax) {
                            chunkData[arrayExpansion] = 2;
                        }
                    }
                }
            }
        }
    }

    public void generateRenderData(int xIn, int zIn) {
        int worldX = chunkPos.x * 16;
        int worldZ = chunkPos.y * 16;

        vertexData = new ArrayList<>();
        elements = new ArrayList<>();

        int vertexCursor = 0;
        int elementCursor = 0;
        int elementIndexCursor = 0;
        int uvCursor = 0;

        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            for (int x = 0; x < CHUNK_DEPTH; x++) {
                for (int z = 0; z < CHUNK_WIDTH; z++) {

                    final int arrayExpansion = vec3toArrayIndex(x, y, z);
                    int id = chunkData[arrayExpansion];

                    if (id == 0) continue;

                    BlockData blockData = LoomObjects.blocks.get(id);
                    TextureData textureData = LoomObjects.textureData.get(blockData.name);

                    Vertex[] verts = new Vertex[8];
                    verts[0] = new Vertex(x + worldX, y, z + worldZ);
                    verts[1] = new Vertex(verts[0].x + 1, verts[0].y, verts[0].z);
                    verts[2] = new Vertex(verts[1].x, verts[1].y, verts[1].z - 1);
                    verts[3] = new Vertex(verts[0].x, verts[0].y, verts[0].z - 1);

                    verts[4] = new Vertex(verts[0].x, verts[0].y - 1, verts[0].z);
                    verts[5] = new Vertex(verts[1].x, verts[1].y - 1, verts[1].z);
                    verts[6] = new Vertex(verts[2].x, verts[2].y - 1, verts[2].z);
                    verts[7] = new Vertex(verts[3].x, verts[3].y - 1, verts[3].z);

                    //top
                    if (drawFace(x, y + 1, z)) {
                        createFace(Face.TOP, vertexData, verts[0], verts[1], verts[2], verts[3], elements, elementIndexCursor, elementCursor, vertexCursor, blockData);
                        vertexCursor += 4;
                        elementIndexCursor += 6;
                        elementCursor += 4;
                    }

                    //right
                    if (drawFace(x, y, z + 1)) {
                        createFace(Face.RIGHT, vertexData, verts[0], verts[4], verts[5], verts[1], elements, elementIndexCursor, elementCursor, vertexCursor, blockData);
                        vertexCursor += 4;
                        elementIndexCursor += 6;
                        elementCursor += 4;
                    }

                    //front
                    if (drawFace(x + 1, y, z)) {
                        createFace(Face.FRONT, vertexData, verts[1], verts[5], verts[6], verts[2], elements, elementIndexCursor, elementCursor, vertexCursor, blockData);
                        vertexCursor += 4;
                        elementIndexCursor += 6;
                        elementCursor += 4;
                    }

                    //left
                    if (drawFace(x, y, z - 1)) {
                        createFace(Face.LEFT, vertexData, verts[2], verts[6], verts[7], verts[3], elements, elementIndexCursor, elementCursor, vertexCursor, blockData);
                        vertexCursor += 4;
                        elementIndexCursor += 6;
                        elementCursor += 4;
                    }

                    //back
                    if (drawFace(x - 1, y, z)) {
                        createFace(Face.BACK, vertexData, verts[3], verts[7], verts[4], verts[0], elements, elementIndexCursor, elementCursor, vertexCursor, blockData);
                        vertexCursor += 4;
                        elementIndexCursor += 6;
                        elementCursor += 4;
                    }

                    //bottom
                    if (drawFace(x, y - 1, z)) {
                        createFace(Face.BOTTOM, vertexData, verts[7], verts[6], verts[5], verts[4], elements, elementIndexCursor, elementCursor, vertexCursor, blockData);
                        vertexCursor += 4;
                        elementIndexCursor += 6;
                        elementCursor += 4;
                    }
                }
            }
        }
        renderData = new ChunkRenderData(vertexData, elements, vertexCursor, elementIndexCursor);
    }

    private int vec3toArrayIndex(int x, int y, int z) {
        int index = (x * CHUNK_DEPTH) + (y * CHUNK_HEIGHT) + z;
        return x >= 16 || x < 0 || z >= 16 || z < 0 || y >= CHUNK_HEIGHT || y < 0 ? 0 : index;
    }

    private boolean drawFace(int x, int y, int z) {
        int blockAt = vec3toArrayIndex(x, y, z);
        int id = chunkData[blockAt];
        return blockAt == 0 || LoomObjects.blocks.get(id).transparent;
    }

    private void createFace(Face face, List<Vertex> vertexData, Vertex vert1, Vertex vert2, Vertex vert3, Vertex vert4, ArrayList<Integer> elements, int elementIndexCursor, int elementCursor, int vertexCursor, BlockData blockData) {
        vertexData.add(vertexCursor, new Vertex(0, 0, 0));
        vertexData.add(vertexCursor + 1, new Vertex(0, 0, 0));
        vertexData.add(vertexCursor + 2, new Vertex(0, 0, 0));
        vertexData.add(vertexCursor + 3, new Vertex(0, 0, 0));

        vertexData.get(vertexCursor).posFrom(vert1);
        vertexData.get(vertexCursor + 1).posFrom(vert2);
        vertexData.get(vertexCursor + 2).posFrom(vert3);
        vertexData.get(vertexCursor + 3).posFrom(vert4);

        elements.add(elementIndexCursor, elementCursor);
        elements.add(elementIndexCursor + 1, elementCursor + 1);
        elements.add(elementIndexCursor + 2, elementCursor + 2);

        elements.add(elementIndexCursor + 3, elementCursor);
        elements.add(elementIndexCursor + 4, elementCursor + 2);
        elements.add(elementIndexCursor + 5, elementCursor + 3);

        TextureData textureFormat = switch (face) {
            case TOP -> LoomObjects.textureData.get(blockData.top);
            case BOTTOM -> LoomObjects.textureData.get(blockData.bottom);
            case LEFT -> LoomObjects.textureData.get(blockData.left);
            case RIGHT -> LoomObjects.textureData.get(blockData.right);
            case FRONT -> LoomObjects.textureData.get(blockData.front);
            case BACK -> LoomObjects.textureData.get(blockData.back);
        };


        vertexData.get(vertexCursor).uvFrom(textureFormat.uvs.get(0).x(), textureFormat.uvs.get(0).y());
        vertexData.get(vertexCursor + 1).uvFrom(textureFormat.uvs.get(1).x(), textureFormat.uvs.get(1).y());
        vertexData.get(vertexCursor + 2).uvFrom(textureFormat.uvs.get(2).x(), textureFormat.uvs.get(2).y());
        vertexData.get(vertexCursor + 3).uvFrom(textureFormat.uvs.get(3).x(), textureFormat.uvs.get(3).y());

        vertexData.get(vertexCursor).face = face.id;
        vertexData.get(vertexCursor + 1).face = face.id;
        vertexData.get(vertexCursor + 2).face = face.id;
        vertexData.get(vertexCursor + 3).face = face.id;
    }

    public void serialize(String path) {
        var time = timeCall(() -> {
            tryCatch(() -> {
                FileOutputStream stream = new FileOutputStream(path);
                ObjectOutputStream out = new ObjectOutputStream(stream);
                out.writeObject(this.chunkData);
                out.close();
            });
        });
        log("Took %sms to serialize chunk.", time);
    }

    public void deserialize(String path, int x, int z) {
        chunkPos = new Vector2i(x, z);
        var time = timeCall(() -> {
            tryCatch(() -> {
                FileInputStream fileIn = new FileInputStream(path);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                this.chunkData = (byte[]) in.readObject();
                in.close();
                fileIn.close();
            });
        });
    }

    public void cleanData() {
        this.vertexData.clear();
        this.elements.clear();
        this.vertexData = null;
        this.elements = null;
        System.gc();
    }

    public enum Face {
        TOP(3), RIGHT(1), FRONT(5), LEFT(0), BACK(4), BOTTOM(2);

        float id;

        Face(float id) {
            this.id = id;
        }
    }
}
