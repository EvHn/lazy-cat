package lazy.cat.cache;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ObjectCacheTest {

    private ObjectCache cache;
    private String METHOD = "method";

    @Before
    public void setUp() {
        cache = new ObjectCache();
    }

    @Test
    public void methodSTD() {
        cache.addMethod(METHOD, -1, -1);
        cache.lazyCall(METHOD, List.of(1, 2), () -> "result1");
        cache.lazyCall(METHOD, List.of(2, 1), () -> "result2");
        Map<String, MethodCache> map = cache.getCache();
        assertEquals("result1", map.get(METHOD).get(List.of(1, 2)).get());
        assertEquals("result2", map.get(METHOD).get(List.of(2, 1)).get());
    }

    @Test
    public void methodLT() throws InterruptedException {
        cache.addMethod(METHOD, 5, -1);
        cache.lazyCall(METHOD, List.of(1, 2), () -> "result1");
        cache.lazyCall(METHOD, List.of(2, 1), () -> "result2");
        MethodCache methodCache = cache.getCache().get(METHOD);
        Map<Object, Object> map = methodCache.getCache();
        assertEquals(2, methodCache.getCache().size());
        assertEquals("result1", ((Pair<Date, Object>) map.get(List.of(1, 2))).getValue());
        assertEquals("result2", ((Pair<Date, Object>) map.get(List.of(2, 1))).getValue());
        Thread.sleep(10);
        assertTrue(methodCache.get(List.of(1, 2)).isEmpty());
        assertTrue(methodCache.get(List.of(2, 1)).isEmpty());
    }

    @Test
    public void methodCap() {
        cache.addMethod(METHOD, -1, 3);
        cache.lazyCall(METHOD, List.of(1, 1), () -> "result1");
        cache.lazyCall(METHOD, List.of(1, 2), () -> "result2");
        cache.lazyCall(METHOD, List.of(1, 3), () -> "result3");
        cache.lazyCall(METHOD, List.of(1, 1), () -> "result4");
        cache.lazyCall(METHOD, List.of(1, 5), () -> "result5");
        cache.lazyCall(METHOD, List.of(1, 6), () -> "result6");
        cache.lazyCall(METHOD, List.of(1, 5), () -> "result7");
        Map<String, MethodCache> map = cache.getCache();
        MethodCache methodCache = map.get(METHOD);
        assertEquals(3, methodCache.getCache().size());
        assertEquals("result1", methodCache.get(List.of(1, 1)).get());
        assertEquals("result5", methodCache.get(List.of(1, 5)).get());
        assertEquals("result6", methodCache.get(List.of(1, 6)).get());
    }

    @Test
    public void methodLTCap() throws InterruptedException {
        cache.addMethod(METHOD, 5, 3);
        cache.lazyCall(METHOD, List.of(1, 1), () -> "result1");
        cache.lazyCall(METHOD, List.of(1, 2), () -> "result2");
        cache.lazyCall(METHOD, List.of(1, 3), () -> "result3");
        cache.lazyCall(METHOD, List.of(1, 1), () -> "result4");
        cache.lazyCall(METHOD, List.of(1, 5), () -> "result5");
        cache.lazyCall(METHOD, List.of(1, 6), () -> "result6");
        cache.lazyCall(METHOD, List.of(1, 5), () -> "result7");
        MethodCache methodCache = cache.getCache().get(METHOD);
        assertEquals(3, methodCache.getCache().size());
        Map<Object, Object> map = methodCache.getCache();
        assertEquals("result1", ((Pair<Date, Object>) map.get(List.of(1, 1))).getValue());
        assertEquals("result5", ((Pair<Date, Object>) map.get(List.of(1, 5))).getValue());
        assertEquals("result6", ((Pair<Date, Object>) map.get(List.of(1, 6))).getValue());
        Thread.sleep(10);
        assertTrue(methodCache.get(List.of(1, 1)).isEmpty());
        assertTrue(methodCache.get(List.of(1, 6)).isEmpty());
        assertTrue(methodCache.get(List.of(1, 5)).isEmpty());
    }
}