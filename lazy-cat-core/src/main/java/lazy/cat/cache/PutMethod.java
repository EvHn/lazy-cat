package lazy.cat.cache;

import java.util.Map;

/**
 * @author EvHn
 */
@FunctionalInterface
public interface PutMethod {
    void call(Map<Object, Object> cache, Object key, Object val);
}
