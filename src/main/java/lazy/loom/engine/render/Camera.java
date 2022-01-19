package lazy.loom.engine.render;

import lazy.loom.engine.input.Keyboard;
import lazy.loom.engine.input.Mouse;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static java.lang.Math.toRadians;
import static lazy.loom.engine.util.math.MathHelper.add;
import static lazy.loom.engine.util.math.MathHelper.cpy;
import static org.joml.Math.cos;
import static org.joml.Math.sin;
import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    private final float fov = (float) toRadians(70f);
    public final Vector3f position = new Vector3f(0, 0, 3f);
    private final Vector3f direction = new Vector3f(0, -90f, 0);
    private Vector3f front = new Vector3f();

    private final float sensitivity = .1f;

    private final Matrix4f projMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();

    public Matrix4f getViewMatrix() {
        var tmpVec = new Vector3f();
        tmpVec.x = (float) (cos(toRadians(direction.y)) * cos(toRadians(direction.x)));
        tmpVec.y = (float) sin(toRadians(direction.x));
        tmpVec.z = (float) (sin(toRadians(direction.y)) * cos(toRadians(direction.x)));

        this.front = tmpVec.normalize();

        var localRight = cpy(front).cross(0, 1f, 0);
        var localUp = cpy(localRight).cross(front);
        viewMatrix.identity();
        return viewMatrix.lookAt(position, add(position, front), localUp);
    }

    public Matrix4f getProjMatrix() {
        projMatrix.identity();
        return projMatrix.perspective(fov, 1280f / 720f, .1f, 1000f);
    }

    public void update() {
        var dx = Mouse.deltaMouseX * sensitivity;
        var dy = Mouse.deltaMouseY * sensitivity;

        direction.x += dy;
        direction.y += dx;

        if (direction.x > 89f) direction.x = 89f;
        if (direction.x < -89f) direction.x = -89f;

        if (Keyboard.isPressed(GLFW_KEY_W)) {
            var localRight = front;
            position.x += localRight.x * .2f;
            position.y += localRight.y * .2f;
            position.z += localRight.z * .2f;
        } else if (Keyboard.isPressed(GLFW_KEY_S)) {
            var localRight = front;
            position.x -= localRight.x * .2f;
            position.y -= localRight.y * .2f;
            position.z -= localRight.z * .2f;
        }

        if (Keyboard.isPressed(GLFW_KEY_A)) {
            var localRight = cpy(front).cross(0f, 1f, 0f);
            position.x -= localRight.x * .2f;
            position.y -= localRight.y * .2f;
            position.z -= localRight.z * .2f;
        } else if (Keyboard.isPressed(GLFW_KEY_D)) {
            var localRight = cpy(front).cross(0f, 1f, 0f);
            position.x += localRight.x * .2f;
            position.y += localRight.y * .2f;
            position.z += localRight.z * .2f;
        }

        if (Keyboard.isPressed(GLFW_KEY_SPACE)) {
            position.y += .2f;
        } else if (Keyboard.isPressed(GLFW_KEY_LEFT_SHIFT)) {
            position.y -= .2f;
        }
    }
}
