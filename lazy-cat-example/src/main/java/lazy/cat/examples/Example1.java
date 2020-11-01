package lazy.cat.examples;

import lazy.cat.annotations.LazyMethod;
import lazy.cat.annotations.LazyObject;

@LazyObject
public class Example1 {

    private String name;

    public Example1() {
    }

    public Example1(String name) {
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
