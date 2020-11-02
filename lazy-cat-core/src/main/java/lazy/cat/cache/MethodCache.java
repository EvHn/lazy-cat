package lazy.cat.cache;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author EvHn
 */
public class MethodCache {
    protected final Map<Object, Object> cache;
    private final PutMethod put;
    private final GetMethod get;

    public MethodCache(Pair<PutMethod, GetMethod> putGet) {
        this.cache = new HashMap<>();
        this.put = putGet.getLeft();
        this.get = putGet.getRight();
    }

    public Optional<Object> get(Object key) {
        return Optional.ofNullable(get.call(cache, key));
    }

    public void put(Object key, Object val) {
        put.call(cache, key, val);
    }
}
