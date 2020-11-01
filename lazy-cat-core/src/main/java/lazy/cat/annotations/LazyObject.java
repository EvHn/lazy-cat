package lazy.cat.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface LazyObject {
    String packageName() default "lazy.cat.sources";
    String postfix() default "Lazy";
    String prefix() default "";
}
