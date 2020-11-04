package lazy.cat.cache;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author EvHn
 */
public class ObjectCache {
    private Map<String, MethodCache> cache = new HashMap<>();

    public void addMethod(String methodName, long lifetime, int capacity) {
        cache.put(methodName, new MethodCache(Objects.requireNonNull(PutGetFactory.createMethods(lifetime, capacity))));
    }

    public Object lazyCall(String methodName, List list, Supplier supplier) {
        Optional<Object> opt = get(methodName, list);
        if(opt.isPresent()) return opt.get();
        Object obj = supplier.get();
        put(methodName, list, obj);
        return obj;
    }

    Map<String, MethodCache> getCache() {
        return cache;
    }

    private Optional<Object> get(String methodName, List<Object> list) {
        return cache.get(methodName).get(list);
    }

    private void put(String methodName, List<Object> list, Object val) {
        cache.get(methodName).put(list, val);
    }
}
