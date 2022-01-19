package lazy.loom.engine;

import lazy.loom.engine.util.Window;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import static lazy.loom.engine.util.Assert.assertOr;
import static lazy.loom.engine.util.Assert.notNull;
import static lazy.loom.engine.util.Log.log;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class Engine {

    private static final String VERSION = "0.0.1";

    public Engine(Game game) {
        init(game);
    }

    private void init(Game game) {
        GLFWErrorCallback.createPrint(System.err).set();

        boolean success = glfwInit();
        assertOr(success, "Unable to initialize GLFW.");

        var windowHandle = Window.create(1280, 720, "Loom");
        Loom.windowHandle = windowHandle;

        logInfo();

        game.create();
        assertOr(game.getScreen() != null, "Game don't have a screen to use. Make sure to call `Game::setScreen` on `Game::create()`");

        float lastFrame = 0f;
        while (!glfwWindowShouldClose(windowHandle.id())) {
            float currentFrame = (float) glfwGetTime();
            Loom.deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;
            game.render(Loom.deltaTime);
            glfwSwapBuffers(windowHandle.id());
            glfwPollEvents();
        }

        game.dispose();

        glfwFreeCallbacks(windowHandle.id());
        glfwDestroyWindow(windowHandle.id());
        glfwTerminate();
        notNull(glfwSetErrorCallback(null)).free();
    }

    private void logInfo() {
        log("Engine initializing.");
        log("\t> GLFW: %s", glfwGetVersionString());
        log("\t> LWJGL: %s", Version.getVersion());
        log("\t> OpenGL Info:");
        log("\t\t> Version: %s", GL20.glGetString(GL11.GL_VERSION));
        log("\t\t> Vendor: %s", GL20.glGetString(GL11.GL_VENDOR));
        log("\t\t> Renderer: %s", GL20.glGetString(GL11.GL_RENDERER));
        log("\t\t> Engine: %s", VERSION);
    }
}
