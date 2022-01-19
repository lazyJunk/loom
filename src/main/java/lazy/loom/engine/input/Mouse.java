package lazy.loom.engine.input;

import org.lwjgl.glfw.GLFW;

public class Mouse {

    private static boolean[] pressedButtons = new boolean[8];

    public static double mouseX = 0.0;
    public static double mouseY = 0.0;
    public static double lastMouseX = 0.0;
    public static double lastMouseY = 0.0;
    public static double deltaMouseX = 0.0;
    public static double deltaMouseY = 0.0;
    private static boolean firstMouse = true;

    public static void createMousePosCallback(long window, double posX, double posY) {
        mouseX = posX;
        mouseY = posY;

        if (firstMouse) {
            lastMouseX = posX;
            lastMouseY = posY;
            firstMouse = false;
        }

        deltaMouseX = posX - lastMouseX;
        deltaMouseY = lastMouseY - posY;

        lastMouseX = posX;
        lastMouseY = posY;
    }

    public static void endFrame() {
        deltaMouseX = 0f;
        deltaMouseY = 0f;
    }

    public static void createMouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW.GLFW_PRESS) {
            pressedButtons[button] = true;
        } else if (button == GLFW.GLFW_RELEASE) {
            pressedButtons[button] = false;
        }
    }
}
