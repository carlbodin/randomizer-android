package carl.coding;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.Random;

public class NumberActivity extends BaseActivity {

    private static final String KEY_NUMBER_MIN = "number_min";
    private static final String KEY_NUMBER_MAX = "number_max";
    private static final float RESULT_TEXT_SIZE_SP = 42f;

    private final Random random = new Random();
    private EditText minInput;
    private EditText maxInput;
    private TextView resultText;
    private Button generateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        minInput = findViewById(R.id.min_input);
        maxInput = findViewById(R.id.max_input);
        resultText = findViewById(R.id.number_result_text);
        generateButton = findViewById(R.id.generate_number_button);

        minInput.setText(PreferencesHelper.getString(this, KEY_NUMBER_MIN, ""));
        maxInput.setText(PreferencesHelper.getString(this, KEY_NUMBER_MAX, ""));

        generateButton.setOnClickListener(v -> generateNumber());
        maxInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_GO) {
                return false;
            }
            hideKeyboard(v);
            generateNumber();
            return true;
        });
    }

    private void generateNumber() {
        String minText = minInput.getText().toString().trim();
        String maxText = maxInput.getText().toString().trim();

        if (TextUtils.isEmpty(minText) || TextUtils.isEmpty(maxText)) {
            minInput.setError(TextUtils.isEmpty(minText) ? getString(R.string.number_required) : null);
            maxInput.setError(TextUtils.isEmpty(maxText) ? getString(R.string.number_required) : null);
            Toast.makeText(this, R.string.number_enter_both, Toast.LENGTH_SHORT).show();
            return;
        }

        minInput.setError(null);
        maxInput.setError(null);

        Long parsedMin = parseWholeNumber(minText);
        Long parsedMax = parseWholeNumber(maxText);
        if (parsedMin == null || parsedMax == null) {
            minInput.setError(parsedMin == null ? getString(R.string.number_invalid) : null);
            maxInput.setError(parsedMax == null ? getString(R.string.number_invalid) : null);
            Toast.makeText(this, R.string.number_use_whole, Toast.LENGTH_SHORT).show();
            return;
        }
        long min = parsedMin;
        long max = parsedMax;

        if (min >= max) {
            maxInput.setError(getString(R.string.number_max_greater));
            Toast.makeText(this, R.string.number_max_greater_toast, Toast.LENGTH_SHORT).show();
            return;
        }

        PreferencesHelper.setString(this, KEY_NUMBER_MIN, minText);
        PreferencesHelper.setString(this, KEY_NUMBER_MAX, maxText);

        generateButton.setEnabled(false);
        generateButton.setText(R.string.number_generating);
        resultText.setTextColor(ContextCompat.getColor(this, R.color.text_feedback_muted));
        resultText.setTextSize(RESULT_TEXT_SIZE_SP);
        resultText.setText("...");

        vibrate(60);
        runRollingAnimation(min, max, 10, 70);
    }

    private void runRollingAnimation(long min, long max, int steps, long delayMs) {
        handler.postDelayed(new Runnable() {
            int remainingSteps = steps;

            @Override
            public void run() {
                resultText.setText(String.valueOf(nextRandomLong(min, max)));

                if (remainingSteps > 0) {
                    remainingSteps--;
                    handler.postDelayed(this, delayMs);
                } else {
                    finishNumberRoll(min, max);
                }
            }
        }, delayMs);
    }

    private void finishNumberRoll(long min, long max) {
        long result = nextRandomLong(min, max);
        resultText.setText(String.valueOf(result));
        resultText.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        resultText.setTextSize(RESULT_TEXT_SIZE_SP);
        resultText.animate().cancel();
        resultText.setScaleX(0.92f);
        resultText.setScaleY(0.92f);
        resultText.animate().scaleX(1f).scaleY(1f).setDuration(140).start();

        generateButton.setEnabled(true);
        generateButton.setText(R.string.number_generate);

        vibrate(140);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private long nextRandomLong(long min, long max) {
        return RandomRange.between(random, min, max);
    }

    private static Long parseWholeNumber(String text) {
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected void onPause() {
        PreferencesHelper.setString(this, KEY_NUMBER_MIN, minInput.getText().toString().trim());
        PreferencesHelper.setString(this, KEY_NUMBER_MAX, maxInput.getText().toString().trim());
        super.onPause();
    }
}
