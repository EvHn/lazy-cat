package lazy.cat.examples;


import lazy.cat.annotations.LazyMethod;

public class Example2 {
    @LazyMethod
    String getVal() {
        return "val";
    }
}
