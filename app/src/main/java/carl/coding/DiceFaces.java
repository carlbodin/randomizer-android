package carl.coding;

/** Maps a die value (1-6) to its face drawable. */
final class DiceFaces {

    private static final int[] FACES = {
            R.drawable.dice_1_512,
            R.drawable.dice_2_512,
            R.drawable.dice_3_512,
            R.drawable.dice_4_512,
            R.drawable.dice_5_512,
            R.drawable.dice_6_512,
    };

    private DiceFaces() {
    }

    static int forValue(int value) {
        return FACES[value - 1];
    }
}
