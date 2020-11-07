package lazy.cat.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author EvHn
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface LazyObject {
    /**
     * @return package of the class name
     */
    String packageName() default "lazy.cat.sources";
    /**
     * @return postfix of the class name
     */
    String postfix() default "Lazy";
    /**
     * @return prefix of the class name
     */
    String prefix() default "";
    /**
     * @return init method prefix
     */
    String initPrefix() default "";
    /**
     * @return true if you want to be able to access the object in concurrency mode
     */
    boolean synchronize() default false;
    /**
     * @return standard cache capacity of methods
     */
    int cacheCapacity() default -1;
    /**
     * @return standard lifetime of values in cache
     */
    long cacheLifetime() default -1L;
}
