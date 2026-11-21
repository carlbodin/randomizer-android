package carl.coding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.Random;

public class DiceActivity extends BaseActivity {

    private static final int ROLL_STEPS = 6;
    private static final String STATE_ROLL = "roll";

    private final Random random = new Random();
    private Button rollButton;
    private ImageView diceImageView;
    private TextView rollResult;
    private int currentRoll = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        rollResult = findViewById(R.id.rollResult);
        rollButton = findViewById(R.id.rollButton);
        diceImageView = findViewById(R.id.diceImageView);

        if (savedInstanceState != null) {
            currentRoll = savedInstanceState.getInt(STATE_ROLL, 1);
            showRoll();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ROLL, currentRoll);
    }

    public void roll(View view) {
        rollButton.setEnabled(false);
        rollResult.setTextColor(ContextCompat.getColor(this, R.color.text_feedback_muted));
        vibrate(300);

        // Each step shows a face different from the previous one; the last step is the result.
        for (int step = 0; step <= ROLL_STEPS; step++) {
            handler.postDelayed(() -> {
                diceImageView.setScaleX(0.8f);
                diceImageView.setScaleY(0.8f);
                int next = random.nextInt(5) + 1;
                currentRoll = next >= currentRoll ? next + 1 : next;
                showRoll();
            }, stepDelay(step));
        }

        handler.postDelayed(() -> {
            diceImageView.setScaleX(1f);
            diceImageView.setScaleY(1f);
            rollResult.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            rollButton.setEnabled(true);
        }, stepDelay(ROLL_STEPS));
    }

    /** Delays grow with each step so the roll visibly slows down. */
    private static int stepDelay(int step) {
        return 70 * step + (step + 10) * (step + 10);
    }

    private void showRoll() {
        diceImageView.setImageResource(DiceFaces.forValue(currentRoll));
        rollResult.setText(String.valueOf(currentRoll));
    }
}
