package lazy.loom.engine.util;

import lazy.loom.engine.util.func.EmptyFunction;
import lazy.loom.engine.util.func.EmptyFunctionExc;
import lazy.loom.engine.util.func.VoidFunction;

import java.io.File;
import java.io.FileWriter;

import static lazy.loom.engine.util.Assert.tryCatch;

public class FuncHelper {

    public static void useWriter(File file, VoidFunction<FileWriter> function) {
        tryCatch(()-> {
            FileWriter writer = new FileWriter(file);
            function.apply(writer);
            writer.close();
        });
    }

    public static long timeCall(EmptyFunctionExc function) {
        long startTime = System.currentTimeMillis();
        tryCatch(function);
        return System.currentTimeMillis() - startTime;
    }
}
