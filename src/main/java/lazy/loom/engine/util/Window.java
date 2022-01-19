package lazy.loom.engine.util;

import lazy.loom.engine.input.Keyboard;
import lazy.loom.engine.input.Mouse;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    public record WindowHandle(long id) {
        public static WindowHandle Zero = new WindowHandle(0);
    }

    public static boolean vSync = true;

    public static WindowHandle create(int width, int height, String title) {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        long id = glfwCreateWindow(width, height, title, 0, 0);
        assert id != 0 : "Failed to create the GLFW window.";

        glfwSetKeyCallback(id, Keyboard::createKeyCallback);
        glfwSetMouseButtonCallback(id, Mouse::createMouseButtonCallback);
        glfwSetCursorPosCallback(id, Mouse::createMousePosCallback);

        glfwShowWindow(id);

        glfwMakeContextCurrent(id);
        if (vSync) glfwSwapInterval(1);

        GL.createCapabilities();

        return new WindowHandle(id);
    }
}
