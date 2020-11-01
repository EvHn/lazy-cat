package lazy.cat.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ObjectCache {
    private Map<String, MethodCache> cache = new HashMap<>();

    public void addMethod(String methodName) {
        cache.put(methodName, new MethodCache());
    }

    public Optional<Object> get(String methodName, List<Object> list) {
        return cache.get(methodName).get(list);
    }

    public void put(String methodName, List<Object> list, Object val) {
        cache.get(methodName).put(list, val);
    }
}
