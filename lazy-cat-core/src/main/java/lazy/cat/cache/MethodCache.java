package lazy.cat.cache;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author EvHn
 */
class MethodCache {
    private final Map<Object, Object> cache;
    private final PutMethod put;
    private final GetMethod get;

    MethodCache(Pair<PutMethod, GetMethod> putGet) {
        this.cache = new HashMap<>();
        this.put = putGet.getLeft();
        this.get = putGet.getRight();
    }

    Optional<Object> get(Object key) {
        return Optional.ofNullable(get.call(cache, key));
    }

    void put(Object key, Object val) {
        put.call(cache, key, val);
    }

    Map<Object, Object> getCache() {
        return cache;
    }
}
