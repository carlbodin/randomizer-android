package carl.coding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Random;

public class CategoriesActivity extends BaseActivity {

    static final String EXTRA_ELEMENTS = "elements";
    static final String EXTRA_WITHDRAW_MODE = "withdrawMode";
    private static final String STATE_POOL = "pool";

    private static final int ROLL_STEPS = 8;
    private static final int ROLL_STEP_MS = 100;

    private final Random random = new Random();
    private ArrayList<String> originalList;
    private ArrayList<String> activeList;
    private boolean withdrawMode;
    private TextView resultText;
    private TextView poolStatusText;
    private Button pickButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        originalList = getIntent().getStringArrayListExtra(EXTRA_ELEMENTS);
        if (originalList == null) {
            originalList = new ArrayList<>();
        }
        withdrawMode = getIntent().getBooleanExtra(EXTRA_WITHDRAW_MODE, false);
        ArrayList<String> savedPool = savedInstanceState == null
                ? null : savedInstanceState.getStringArrayList(STATE_POOL);
        activeList = savedPool != null ? savedPool : new ArrayList<>(originalList);

        resultText = findViewById(R.id.result_text);
        poolStatusText = findViewById(R.id.pool_status_text);
        pickButton = findViewById(R.id.pick_button);
        Button resetButton = findViewById(R.id.reset_button);

        pickButton.setOnClickListener(v -> pick());
        resetButton.setOnClickListener(v -> resetPool());
        resetButton.setVisibility(withdrawMode ? View.VISIBLE : View.GONE);
        pickButton.setEnabled(!activeList.isEmpty());
        updatePoolStatus();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(STATE_POOL, activeList);
    }

    private void pick() {
        if (activeList.isEmpty()) {
            return;
        }
        pickButton.setEnabled(false);
        resultText.setTextColor(ContextCompat.getColor(this, R.color.text_feedback_muted));
        vibrate(250);

        // Cycle through the pool to build suspense, then land on a random pick.
        for (int step = 0; step < ROLL_STEPS; step++) {
            String shown = activeList.get(step % activeList.size());
            handler.postDelayed(() -> resultText.setText(shown), (long) step * ROLL_STEP_MS);
        }
        handler.postDelayed(this::finishPick, (long) ROLL_STEPS * ROLL_STEP_MS);
    }

    private void finishPick() {
        int index = random.nextInt(activeList.size());
        resultText.setText(activeList.get(index));
        resultText.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        if (withdrawMode) {
            activeList.remove(index);
        }
        pickButton.setEnabled(!activeList.isEmpty());
        updatePoolStatus();
    }

    private void resetPool() {
        handler.removeCallbacksAndMessages(null);
        activeList = new ArrayList<>(originalList);
        resultText.setText("Ready");
        resultText.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        pickButton.setEnabled(!activeList.isEmpty());
        updatePoolStatus();
    }

    private void updatePoolStatus() {
        if (!withdrawMode) {
            poolStatusText.setText("Standard mode, picks stay in the pool.");
        } else if (activeList.isEmpty()) {
            poolStatusText.setText("Withdraw mode on, all categories have been used.");
        } else {
            poolStatusText.setText("Withdraw mode on, " + activeList.size() + " left in the pool.");
        }
    }
}
