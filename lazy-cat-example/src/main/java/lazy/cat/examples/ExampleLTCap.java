package lazy.cat.examples;

import lazy.cat.annotations.LazyMethod;
import lazy.cat.annotations.LazyObject;

/**
 * @author EvHn
 */
@LazyObject(cacheLifetime = 1000L, cacheCapacity = 10)
public class ExampleLTCap {
    private String name;

    @LazyMethod(cacheLifetime = -1)
    public String getName() {
        return name;
    }

    @LazyMethod(cacheLifetime = 500L, cacheCapacity = 100)
    public int getValue() {
        return 0;
    }

    @LazyMethod
    public String getSameString(String srt) {
        return srt;
    }

    @LazyMethod(cacheLifetime = 1001L, cacheCapacity = -1)
    public String cat(String str1, String str2) {
        return str1 + str2;
    }
}
