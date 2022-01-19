package lazy.loom.engine.util.math;

import org.joml.Vector3f;
import org.joml.Vector3i;

public class MathHelper {

    public static Vector3f cpy(Vector3f other) {
        return new Vector3f(other.x, other.y, other.z);
    }

    public static Vector3i cpy(Vector3i other) {
        return new Vector3i(other.x, other.y, other.z);
    }

    public static Vector3f add(Vector3f one, Vector3f add) {
        return new Vector3f(one.x + add.x, one.y + add.y, one.z + add.z);
    }

    public static Vector3f sub(Vector3f one, Vector3f sub) {
        return new Vector3f(one.x - sub.x, one.y - sub.y, one.z - sub.z);
    }

    public static float mapRange(float val, float inMin, float inMax, float outMin, float outMax)
    {
        return (val - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }
}
