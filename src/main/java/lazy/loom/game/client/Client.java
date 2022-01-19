package lazy.loom.game.client;

import lazy.loom.ClientGame;
import lazy.loom.engine.Engine;

public class Client {

    public static void main(String[] args) {
        new Engine(new ClientGame());
    }
}
