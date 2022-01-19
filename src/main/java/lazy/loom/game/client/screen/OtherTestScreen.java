package lazy.loom.game.client.screen;

import lazy.loom.engine.Loom;
import lazy.loom.engine.Screen;
import lazy.loom.engine.input.Mouse;
import lazy.loom.engine.render.Camera;
import lazy.loom.engine.render.Shader;
import lazy.loom.engine.render.Texture;
import lazy.loom.engine.util.AtlasTexture;
import lazy.loom.game.common.world.chunk.ChunkRenderer;
import lazy.loom.game.common.world.chunk.IntChunk;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static lazy.loom.engine.Loom.*;
import static lazy.loom.engine.util.Assert.tryCatch;
import static lazy.loom.engine.util.Log.log;
import static org.lwjgl.opengl.GL11.*;

public class OtherTestScreen implements Screen {

    private Shader shader = new Shader("./assets/vertex.glsl", "./assets/fragment.glsl");
    private Camera camera = new Camera();
    private Texture pink;
    private IntChunk chunk;
    private ChunkRenderer renderer;
    public List<IntChunk> loadedChunks = new ArrayList<>();
    public List<ChunkRenderer> chunkRenderer = new ArrayList<>();
    private Vector3f sunPos = new Vector3f(1f, 100f, 1f);

    @Override
    public void init() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        camera.position.add(0f, 160f, 0f);
        Loom.grabMouse(true);
        tryCatch(AtlasTexture::createAtlas);
        pink = new Texture("./atlas.png");

        var start = System.currentTimeMillis();

        Random rnd = new Random();
        int seed = rnd.nextInt(1337);
        int chunkIndex = 0;
        for (int i = -12; i < 12; i++) {
            for (int j = -12; j < 12; j++) {
                var time = System.currentTimeMillis();
                loadedChunks.add(chunkIndex, new IntChunk());
                String chunkPath = "./data/chunks/chunk-" + i + "_" + j + ".txt";
                if (Files.exists(Paths.get(chunkPath))) {
                    loadedChunks.get(chunkIndex).deserialize(chunkPath, i, j);
                } else {
                    loadedChunks.get(chunkIndex).generateChunk(i, j, seed);
                }
                loadedChunks.get(chunkIndex).generateRenderData(i, j);
                chunkRenderer.add(chunkIndex, new ChunkRenderer());
                chunkRenderer.get(chunkIndex).setup(loadedChunks.get(chunkIndex));
                chunkIndex++;
                log("Took %sms to load chunk at: %s, %s", System.currentTimeMillis() - time, i, j);
            }
        }

        System.out.println(chunkIndex);

        /*chunk = new IntChunk();
        chunk.generateChunk(0, 0, rnd.nextInt(1337));
        chunk.generateRenderData(0, 0);
        renderer = new ChunkRenderer();
        renderer.setup(chunk);*/

        log("Took %sms to generate chunkd.", System.currentTimeMillis() - start);

        //shader.bind();
        shader.i("uTexture", 0);
    }

    @Override
    public void render(float dt) {
        Loom.setWindowTitle("Memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "/" + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + "MiB (max " + (Runtime.getRuntime().maxMemory() / 1024 / 1024) + "MiB)");
        Loom.onPress(GLFW.GLFW_KEY_ESCAPE, Loom::close);
        clearColor(.2f, .2f, 1f, 1f);
        clear(COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        camera.update();
        shader.bind();
        pink.bind();
        shader.mat4("uProj", camera.getProjMatrix());
        shader.mat4("uView", camera.getViewMatrix());
        shader.vec3f("uSunPos", sunPos);
        for (int i = 0; i < this.loadedChunks.size(); i++) {
            var loadedChunk = loadedChunks.get(i);
            //shader.vec2i("uChunkPos", new Vector2i(loadedChunk.chunkPos.x, loadedChunk.chunkPos.z));
            chunkRenderer.get(i).render(loadedChunk);
        }
        //renderer.render(chunk);

        Mouse.endFrame();
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void dispose() {
        /*for (IntChunk loadedChunk : this.loadedChunks) {
            loadedChunk.serialize("./data/chunks/chunk-" + loadedChunk.chunkPos.x + "_" + loadedChunk.chunkPos.y + ".txt");
        }*/
    }
}
