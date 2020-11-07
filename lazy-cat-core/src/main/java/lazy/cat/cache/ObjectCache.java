package lazy.cat.cache;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author EvHn
 */
public class ObjectCache {
    private final Map<String, MethodCache> cache = new HashMap<>();
    private final LazyCallMethod lazyCall;

    public ObjectCache(boolean synchronize) {
        this.lazyCall = LazyCallFactory.createCallMethod(synchronize);
    }

    public void addMethod(String methodName, long lifetime, int capacity) {
        cache.put(methodName, new MethodCache(Objects.requireNonNull(PutGetFactory.createMethods(lifetime, capacity))));
    }

    public Object lazyCall(final String methodName, final List<Object> list, final Supplier<Object> supplier) {
        return lazyCall.call(cache, methodName, list, supplier);
    }

    Map<String, MethodCache> getCache() {
        return cache;
    }

}
