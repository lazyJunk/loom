package lazy.loom.engine;

public interface Screen {
    void init();

    void render(float dt);

    void resize(int width, int height);

    void dispose();
}
