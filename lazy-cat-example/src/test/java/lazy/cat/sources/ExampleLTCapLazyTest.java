package lazy.cat.sources;

import lazy.cat.examples.ExampleLTCap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author EvHn
 */
public class ExampleLTCapLazyTest {

    private ExampleLTCap exampleLTCap;
    @Before
    public void setUp() throws Exception {
        exampleLTCap = new ExampleLTCapLazy();
    }

    @Test
    public void name() {
    }

    @Test
    public void cat() {
        String actual = exampleLTCap.cat("str1", "str2");
        String expected = "str1str2";
        assertEquals(expected, actual);
        actual = exampleLTCap.cat("str1", "str2");
        assertEquals(expected, actual);
    }

    @Test
    public void getSameString() {
        String actual = exampleLTCap.getSameString("str1");
        String expected = "str1";
        assertEquals(expected, actual);
        actual = exampleLTCap.getSameString("str1");
        assertEquals(expected, actual);
    }

    @Test
    public void getValue() {
        int value = exampleLTCap.getValue();
        assertEquals(0, value);
        value = exampleLTCap.getValue();
        assertEquals(0, value);
    }
}