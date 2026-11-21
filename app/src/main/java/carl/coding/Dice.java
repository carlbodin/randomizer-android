package carl.coding;

/** Shared die visuals: face drawables and the timing of the roll animation. */
final class Dice {

    /** Number of intermediate faces shown before a roll settles. */
    static final int ROLL_STEPS = 6;

    private static final int[] FACES = {
            R.drawable.dice_1_512,
            R.drawable.dice_2_512,
            R.drawable.dice_3_512,
            R.drawable.dice_4_512,
            R.drawable.dice_5_512,
            R.drawable.dice_6_512,
    };

    private Dice() {
    }

    /** Drawable for a die value 1-6. */
    static int face(int value) {
        return FACES[value - 1];
    }

    /** Delay before roll step {@code step}; the gaps grow so the roll visibly slows down. */
    static int rollStepDelay(int step) {
        return 70 * step + (step + 10) * (step + 10);
    }
}
