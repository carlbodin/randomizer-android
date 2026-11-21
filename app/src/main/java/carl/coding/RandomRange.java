package carl.coding;

import java.util.Random;

/** Uniform random whole numbers in an inclusive range, exact for the full {@code long} range. */
final class RandomRange {

    private RandomRange() {
    }

    static long between(Random random, long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("min > max");
        }
        long span = max - min + 1;
        if (span <= 0) {
            // The range is wider than Long.MAX_VALUE (or spans every long): at least half
            // of all longs fall inside it, so plain rejection terminates quickly.
            long value;
            do {
                value = random.nextLong();
            } while (value < min || value > max);
            return value;
        }
        // Rejection sampling avoids the modulo bias of nextLong() % span.
        long bits;
        long value;
        do {
            bits = random.nextLong() >>> 1;
            value = bits % span;
        } while (bits - value + (span - 1) < 0);
        return min + value;
    }
}
