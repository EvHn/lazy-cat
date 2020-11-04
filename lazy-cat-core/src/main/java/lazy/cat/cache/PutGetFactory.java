package lazy.cat.cache;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * @author EvHn
 */
class PutGetFactory {

    static Pair<PutMethod, GetMethod> createMethods(long lifetime, int capacity) {
        if(lifetime < 0 && capacity < 0) {
            return createPutAndGet();
        } else if(lifetime > 0 && capacity < 0) {
            return createPutAndGetLT(lifetime);
        } else if(lifetime < 0 && capacity > 0) {
            return createPutAndGetCap(capacity);
        }
        return createPutAndGetCapLT(capacity, lifetime);
    }

    private static Pair<PutMethod, GetMethod> createPutAndGet() {
        return Pair.of(PutGetFactory::put, PutGetFactory::get);
    }

    private static Pair<PutMethod, GetMethod> createPutAndGetLT(long lifetime) {
        return Pair.of(PutGetFactory::putLT, (cache, key) -> getLT(cache, key, lifetime));
    }

    private static Pair<PutMethod, GetMethod> createPutAndGetCap(int capacity) {
        return Pair.of(new PutMethodCap(capacity, PutGetFactory::put), PutGetFactory::get);
    }

    private static Pair<PutMethod, GetMethod> createPutAndGetCapLT(int capacity, long lifetime) {
        return Pair.of(new PutMethodCap(capacity, PutGetFactory::putLT), (cache, key) -> getLT(cache, key, lifetime));
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

    private static void putCap(Map<Object, Object> cache, Object key, Object val, int capacity,
                               LinkedList<Object> queue, PutMethod put) {
        if(queue.size() >= capacity) {
            cache.remove(queue.removeLast());
        }
        put.call(cache, key, val);
        queue.addFirst(key);
    }

    private static class PutMethodCap implements PutMethod {
        private final int capacity;
        private final PutMethod put;
        private final LinkedList<Object> queue = new LinkedList<>();

        public PutMethodCap(int capacity, PutMethod put) {
            this.capacity = capacity;
            this.put = put;
        }

        @Override
        public void call(Map<Object, Object> cache, Object key, Object val) {
            putCap(cache, key, val, capacity, queue, put);
        }
    }
}
