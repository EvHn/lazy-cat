package lazy.cat.sources;

import lazy.cat.examples.ExampleSTD;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author EvHn
 */
public class ExampleSTDLazyTest {

    private ExampleSTD exampleSTD;

    @Before
    public void setUp() {
        exampleSTD = new ExampleSTDLazy();
    }

    @Test
    public void getValue() {
        int value = exampleSTD.getValue();
        assertEquals(0, value);
        value = exampleSTD.getValue();
        assertEquals(0, value);
    }

    @Test
    public void getSameString() {
        String actual = exampleSTD.getSameString("str1");
        String expected = "str1";
        assertEquals(expected, actual);
        actual = exampleSTD.getSameString("str1");
        assertEquals(expected, actual);
    }

    @Test
    public void cat() {
        String actual = exampleSTD.cat("str1", "str2");
        String expected = "str1str2";
        assertEquals(expected, actual);
        actual = exampleSTD.cat("str1", "str2");
        assertEquals(expected, actual);
    }
}