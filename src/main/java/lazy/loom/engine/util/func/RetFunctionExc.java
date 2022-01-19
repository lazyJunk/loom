package lazy.loom.engine.util.func;

@FunctionalInterface
public interface RetFunctionExc<R> {

    R apply() throws Exception;
}
