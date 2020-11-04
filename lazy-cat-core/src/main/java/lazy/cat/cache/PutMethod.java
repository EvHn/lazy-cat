package lazy.cat.cache;

import java.util.Map;

/**
 * @author EvHn
 */
@FunctionalInterface
interface PutMethod {
    void call(Map<Object, Object> cache, Object key, Object val);
}
