package carl.coding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Random;

public class RandomRangeTest {

    private final Random random = new Random(42);

    @Test
    public void staysWithinSmallRangeAndHitsEveryValue() {
        boolean[] seen = new boolean[6];
        for (int i = 0; i < 1000; i++) {
            long value = RandomRange.between(random, 1, 6);
            assertTrue(value >= 1 && value <= 6);
            seen[(int) value - 1] = true;
        }
        for (boolean s : seen) {
            assertTrue(s);
        }
    }

    @Test
    public void staysWithinRangesBeyondDoublePrecision() {
        long min = Long.MAX_VALUE - 3;
        for (int i = 0; i < 1000; i++) {
            long value = RandomRange.between(random, min, Long.MAX_VALUE);
            assertTrue(value >= min);
        }
    }

    @Test
    public void handlesRangesWiderThanLongMax() {
        for (int i = 0; i < 1000; i++) {
            long value = RandomRange.between(random, -10, Long.MAX_VALUE);
            assertTrue(value >= -10);
        }
        RandomRange.between(random, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Test
    public void singleValueRange() {
        assertEquals(7, RandomRange.between(random, 7, 7));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsInvertedRange() {
        RandomRange.between(random, 2, 1);
    }
}
