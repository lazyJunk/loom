package lazy.loom.engine.util;

import java.util.Objects;

public class Log {

    public static void log(String toFormat, Object... args){
        System.out.printf(toFormat.concat("\n"), args);
    }
}
