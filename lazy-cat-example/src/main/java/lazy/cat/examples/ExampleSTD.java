package lazy.cat.examples;

import lazy.cat.annotations.LazyMethod;
import lazy.cat.annotations.LazyObject;

/**
 * @author EvHn
 */
@LazyObject
public class ExampleSTD {

    private String name;

    public ExampleSTD() {
    }

    public ExampleSTD(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @LazyMethod
    public int getValue() {
        return 0;
    }

    @LazyMethod
    public String getSameString(String srt) {
        return srt;
    }

    @LazyMethod
    public String cat(String str1, String str2) {
        return str1 + str2;
    }
}
