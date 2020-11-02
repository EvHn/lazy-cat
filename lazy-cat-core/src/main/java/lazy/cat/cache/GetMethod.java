package lazy.cat.cache;

import java.util.Map;

/**
 * @author EvHn
 */
@FunctionalInterface
public interface GetMethod {
    Object call(Map<Object, Object> cache, Object key);
}
