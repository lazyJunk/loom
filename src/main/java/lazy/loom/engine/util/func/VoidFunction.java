package lazy.loom.engine.util.func;

@FunctionalInterface
public interface VoidFunction<T> {

    void apply(T t);
}
