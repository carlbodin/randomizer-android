package carl.coding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.Random;

/** Five dice; after a roll, tapping a die holds it so the next roll keeps its value. */
public class YatzyActivity extends BaseActivity {

    private static final int DICE = 5;
    private static final String STATE_VALUES = "values";
    private static final String STATE_HELD = "held";
    private static final String STATE_ROLLED = "rolled";

    private final Random random = new Random();
    private final ImageView[] dieImages = new ImageView[DICE];
    private final TextView[] holdLabels = new TextView[DICE];
    private int[] values = {1, 2, 3, 4, 5};
    private boolean[] held = new boolean[DICE];
    private boolean rolled;
    private boolean rolling;
    private Button rollButton;
    private TextView resultText;
    private TextView hintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yatzy);

        rollButton = findViewById(R.id.yatzyRollButton);
        resultText = findViewById(R.id.yatzyRollResult);
        hintText = findViewById(R.id.yatzyHint);
        rollButton.setOnClickListener(v -> roll());

        if (savedInstanceState != null) {
            values = savedInstanceState.getIntArray(STATE_VALUES);
            held = savedInstanceState.getBooleanArray(STATE_HELD);
            rolled = savedInstanceState.getBoolean(STATE_ROLLED);
        }

        LinearLayout row = findViewById(R.id.yatzyDiceRow);
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < DICE; i++) {
            View die = inflater.inflate(R.layout.item_yatzy_die, row, false);
            dieImages[i] = die.findViewById(R.id.die_image);
            holdLabels[i] = die.findViewById(R.id.die_hold_label);
            int index = i;
            die.setOnClickListener(v -> toggleHold(index));
            row.addView(die);
            showDie(i);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(STATE_VALUES, values);
        outState.putBooleanArray(STATE_HELD, held);
        outState.putBoolean(STATE_ROLLED, rolled);
    }

    private void roll() {
        rolling = true;
        rollButton.setEnabled(false);
        resultText.setTextColor(ContextCompat.getColor(this, R.color.text_feedback_muted));
        vibrate(300);

        for (int step = 0; step <= Dice.ROLL_STEPS; step++) {
            handler.postDelayed(() -> {
                for (int i = 0; i < DICE; i++) {
                    if (!held[i]) {
                        values[i] = random.nextInt(6) + 1;
                        dieImages[i].setScaleX(0.8f);
                        dieImages[i].setScaleY(0.8f);
                        showDie(i);
                    }
                }
            }, Dice.rollStepDelay(step));
        }

        handler.postDelayed(() -> {
            int sum = 0;
            for (int i = 0; i < DICE; i++) {
                dieImages[i].setScaleX(1f);
                dieImages[i].setScaleY(1f);
                sum += values[i];
            }
            resultText.setText(getString(R.string.yatzy_sum, sum));
            resultText.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            hintText.setText(R.string.yatzy_hint_hold);
            rolled = true;
            rolling = false;
            rollButton.setEnabled(true);
        }, Dice.rollStepDelay(Dice.ROLL_STEPS));
    }

    private void toggleHold(int index) {
        if (!rolled || rolling) {
            return;
        }
        held[index] = !held[index];
        showDie(index);
    }

    private void showDie(int index) {
        dieImages[index].setImageResource(Dice.face(values[index]));
        dieImages[index].setSelected(held[index]);
        holdLabels[index].setVisibility(held[index] ? View.VISIBLE : View.INVISIBLE);
        dieImages[index].setContentDescription(getString(
                held[index] ? R.string.yatzy_die_held_description : R.string.yatzy_die_description, values[index]));
    }
}
