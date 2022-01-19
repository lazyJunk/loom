package lazy.loom.engine;

import lazy.loom.engine.input.Keyboard;
import lazy.loom.engine.util.Window;
import lazy.loom.engine.util.func.EmptyFunction;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;

import static lazy.loom.engine.util.Assert.notNull;
import static org.lwjgl.glfw.GLFW.*;

public class Loom {

    protected static Window.WindowHandle windowHandle;
    public static final int COLOR_BUFFER_BIT = GL11.GL_COLOR_BUFFER_BIT;

    protected static float deltaTime = -1f;

    private static long windowID() {
        return notNull(windowHandle).id();
    }

    public static void clearColor(float r, float g, float b, float a) {
        GL33.glClearColor(r, g, b, a);
    }

    public static void clear(int mask) {
        GL33.glClear(mask);
    }

    public static void close() {
        GLFW.glfwSetWindowShouldClose(notNull(windowHandle).id(), true);
    }

    public static float getDeltaTime() {
        return deltaTime;
    }

    public static void setWindowTitle(String title) {
        glfwSetWindowTitle(notNull(windowHandle).id(), title);
    }

    public static void grabMouse(boolean enabled) {
        int type = enabled ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL;
        glfwSetInputMode(windowID(), GLFW_CURSOR, type);
    }

    public static void onPress(int key, EmptyFunction doFunc) {
        if(Keyboard.isPressed(key))
            doFunc.apply();
    }
}
