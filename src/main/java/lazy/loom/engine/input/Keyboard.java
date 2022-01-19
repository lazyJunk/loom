package lazy.loom.engine.input;

import org.lwjgl.glfw.GLFW;

public class Keyboard {

    private static final boolean[] pressed = new boolean[GLFW.GLFW_KEY_LAST];

    public static void createKeyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW.GLFW_PRESS) {
            pressed[key] = true;
        } else if (action == GLFW.GLFW_RELEASE) {
            pressed[key] = false;
        }
    }

    public static boolean isPressed(int key) {
        return pressed[key];
    }
}
