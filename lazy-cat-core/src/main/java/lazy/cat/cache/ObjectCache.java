package lazy.cat.cache;

import java.util.*;

/**
 * @author EvHn
 */
public class ObjectCache {
    private Map<String, MethodCache> cache = new HashMap<>();

    public void addMethod(String methodName, long lifetime, int capacity) {
        cache.put(methodName, new MethodCache(Objects.requireNonNull(Utils.createMethods(lifetime, capacity))));
    }

    public Optional<Object> get(String methodName, List<Object> list) {
        return cache.get(methodName).get(list);
    }

    public void put(String methodName, List<Object> list, Object val) {
        cache.get(methodName).put(list, val);
    }
}
