package lazy.loom.engine;

public abstract class Game {

    private Screen screen;

    public abstract void create();

    public void render(float dt) {
        screen.render(dt);
    }

    public void resize(int width, int height) {
        screen.resize(width, height);
    }

    public void dispose() {
        screen.dispose();
    }

    public Screen getScreen() {
        return this.screen;
    }

    public void setScreen(Screen other) {
        if (this.screen != null) this.screen.dispose();
        this.screen = other;
        this.screen.init();
    }
}
