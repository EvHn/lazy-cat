package lazy.cat.cache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author EvHn
 */
public class LazyCallFactory {
    public static LazyCallMethod createCallMethod(boolean synchronize) {
        return synchronize ? (cache, mn, list, sup) -> lazyCallSyn(cache, () -> lazyCall(cache, mn, list, sup))
                : LazyCallFactory::lazyCall;
    }

    private static Object lazyCall(final Map<String, MethodCache> cache, final String methodName,
                                   final List<Object> list, final Supplier<Object> supplier) {
        Optional<Object> opt = cache.get(methodName).get(list);
        if(opt.isPresent()) return opt.get();
        Object obj = supplier.get();
        cache.get(methodName).put(list, obj);
        return obj;
    }

    private static Object lazyCallSyn(Object syn, Supplier<Object> lazyCall) {
        synchronized(syn) {
            return lazyCall.get();
        }
    }
}
