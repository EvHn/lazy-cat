package lazy.cat.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MethodCache {
    private Map<Object, Object> cache = new HashMap<>();

    public Optional<Object> get(Object key) {
        return Optional.ofNullable(cache.get(key));
    }

    public void put(Object key, Object val) {
        cache.put(key, val);
    }
}
