package edu.emory.cs;
// check
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest {
    @Test
    public void getMiddleIndexTest() {
        assertEquals(5, Utils.getMiddleIndex(0, 10));
    }
}
