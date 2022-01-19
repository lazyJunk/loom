package lazy.loom.engine.util;

import lazy.loom.engine.util.func.EmptyFunction;
import lazy.loom.engine.util.func.EmptyFunctionExc;
import lazy.loom.engine.util.func.RetFunctionExc;

import java.util.Objects;

public class Assert {

    public static void assertOr(boolean cond, String msg) {
        if (!cond) {
            System.out.println(msg);
            System.exit(1);
        }
    }

    public static void assertOrDo(boolean cond, EmptyFunction function) {
        if (!cond) {
            function.apply();
        }
    }

    public static void tryCatch(EmptyFunctionExc function) {
        try {
            function.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T tryCatchRet(RetFunctionExc<T> function) {
        try {
            return function.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T notNull(T obj) {
        return Objects.requireNonNull(obj);
    }
}
