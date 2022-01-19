package lazy.loom.game.common.world.block;

import java.io.Serializable;

public class Block implements Serializable {

    public transient static Block AIR = new Block();

    public int id = 0;
    public int lightLevel;
    public int rotation;

}
