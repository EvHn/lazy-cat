package lazy.cat.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author EvHn
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface LazyMethod {
    /**
     * @return standard cache capacity of method.
     * override value in {@code LazyObject}
     */
    int cacheCapacity() default 0;

    /**
     * @return standard lifetime of values in cache
     * override value in {@code LazyObject}
     */
    long cacheLifetime() default 0L;
}
