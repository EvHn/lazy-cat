package lazy.cat.cache;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author EvHn
 */
@FunctionalInterface
public interface LazyCallMethod {
    Object call(final Map<String, MethodCache> cache, final String methodName,
                final List<Object> list, final Supplier<Object> supplier);
}
