package lazy.loom.game.client.screen;

import lazy.loom.engine.Loom;
import lazy.loom.engine.Screen;
import lazy.loom.engine.render.font.BitmapFont;

import static lazy.loom.engine.util.FuncHelper.timeCall;

public class BlueScreen implements Screen {
    @Override
    public void init() {
        var time = timeCall(() -> new BitmapFont("C:/Windows/Fonts/Arial.ttf", 64));
        System.out.println("Took " + time + " ms to generate bitmap font.");
    }

    @Override
    public void render(float dt) {
        Loom.clearColor(0f, 0f, 1f, 1f);
        Loom.clear(Loom.COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }
}
