package carl.coding;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class YesNoActivity extends BaseActivity {

    private final Random random = new Random();
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yes_no);

        resultText = findViewById(R.id.yes_no_result_text);
        Button askButton = findViewById(R.id.ask_yes_no_button);
        askButton.setOnClickListener(v -> pickAnswer());
    }

    private void pickAnswer() {
        resultText.setText(random.nextBoolean() ? R.string.yes : R.string.no);
        vibrate(140);
    }
}
