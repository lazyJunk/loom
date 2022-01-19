package lazy.loom;

import lazy.loom.engine.Game;
import lazy.loom.game.client.screen.BlueScreen;
import lazy.loom.game.client.screen.OtherTestScreen;

public class ClientGame extends Game {

    public void create() {
        setScreen(new OtherTestScreen());
    }

    @Override
    public void render(float dt) {
        super.render(dt);
    }
}
