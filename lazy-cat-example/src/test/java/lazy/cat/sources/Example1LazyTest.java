package lazy.cat.sources;

import lazy.cat.examples.Example1;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Example1LazyTest {

    private Example1 example1;

    @Before
    public void setUp() {
        example1 = new Example1Lazy();
    }

    @Test
    public void getValue() {
        int value = example1.getValue();
        assertEquals(0, value);
        value = example1.getValue();
        assertEquals(0, value);
    }

    @Test
    public void getSameString() {
        String actual = example1.getSameString("str1");
        String expected = "str1";
        assertEquals(expected, actual);
        actual = example1.getSameString("str1");
        assertEquals(expected, actual);
    }

    @Test
    public void cat() {
        String actual = example1.cat("str1", "str2");
        String expected = "str1str2";
        assertEquals(expected, actual);
        actual = example1.cat("str1", "str2");
        assertEquals(expected, actual);
    }
}