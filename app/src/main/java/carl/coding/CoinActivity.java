package carl.coding;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class CoinActivity extends BaseActivity {

    private static final int FLIP_DURATION_MS = 500;
    private static final int LAND_DURATION_MS = 200;

    private final Random random = new Random();
    private Button flipButton;
    private ImageView coinImageView;
    private TextView coinFlipResult;
    private ObjectAnimator flipAnimator;
    private ObjectAnimator landAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        coinImageView = findViewById(R.id.coinImageView);
        coinFlipResult = findViewById(R.id.coinFlipResult);
        flipButton = findViewById(R.id.flipButton);

        flipAnimator = ObjectAnimator.ofFloat(coinImageView, "rotationY", 0f, 360 * 4 + 90f)
                .setDuration(FLIP_DURATION_MS);
        landAnimator = ObjectAnimator.ofFloat(coinImageView, "rotationY", -90f, 0f)
                .setDuration(LAND_DURATION_MS);
    }

    public void flip(View view) {
        flipButton.setEnabled(false);
        coinFlipResult.setText(" ");
        vibrate(300);

        boolean heads = random.nextBoolean();
        flipAnimator.start();
        handler.postDelayed(() -> {
            coinImageView.setImageResource(heads ? R.drawable.coin_heads_transparent : R.drawable.coin_tails_transparent);
            coinFlipResult.setText(heads ? "HEADS" : "TAILS");
            landAnimator.start();
            flipButton.setEnabled(true);
        }, FLIP_DURATION_MS);
    }
}
