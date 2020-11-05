package lazy.cat.cache;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author EvHn
 */
class PutGetFactory {

    static Pair<PutMethod, GetMethod> createMethods(long lifetime, int capacity) {
        Pair<PutMethod, GetMethod> pair = (lifetime < 0) ? createPutAndGet() : createPutAndGetLT(lifetime);
        return (capacity > 0) ? createPutAndGetCap(capacity, pair) : pair;
    }

    private static Pair<PutMethod, GetMethod> createPutAndGet() {
        return Pair.of(PutGetFactory::put, PutGetFactory::get);
    }

    private static Pair<PutMethod, GetMethod> createPutAndGetLT(long lifetime) {
        return Pair.of(PutGetFactory::putLT, (cache, key) -> getLT(cache, key, lifetime));
    }

    private static Pair<PutMethod, GetMethod> createPutAndGetCap(int capacity, Pair<PutMethod, GetMethod> pair) {
        LinkedList<Object> queue = new LinkedList<>();
        return Pair.of((cache, key, val) -> putCap(cache, key, val, capacity, queue::size, queue::removeLast,
                queue::addFirst, pair.getKey()), (cache, key) -> getCap(cache, key, queue::addFirst,
                queue::removeLast, pair.getValue()));
    }

    private static Object get(Map<Object, Object> cache, Object key) {
        return cache.get(key);
    }

    private static void put(Map<Object, Object> cache, Object key, Object val) {
        cache.put(key, val);
    }

    private static Object getLT(Map<Object, Object> cache, Object key, long lifetime) {
        Pair<Date, Object> pair = ((Pair<Date, Object>) cache.remove(key));
        if(pair != null && new Date().getTime() - pair.getKey().getTime() <= lifetime) {
            putLT(cache, key, pair.getValue());
            return pair.getValue();
        }
        return null;
    }

    private static void putLT(Map<Object, Object> cache, Object key, Object val) {
        cache.put(key, Pair.of(new Date(), val));
    }

    private static Object getCap(Map<Object, Object> cache, Object key, Consumer<Object> add, Supplier<Object> poll,
                                 GetMethod get) {
        Object val = get.call(cache, key);
        if(val != null) {
            add.accept(poll.get());
        }
        return val;
    }

    private static void putCap(Map<Object, Object> cache, Object key, Object val, int capacity, Supplier<Integer> size,
                               Supplier<Object> poll, Consumer<Object> add, PutMethod put) {
        if(size.get() >= capacity) {
            cache.remove(poll.get());
        }
        put.call(cache, key, val);
        add.accept(key);
    }
}
