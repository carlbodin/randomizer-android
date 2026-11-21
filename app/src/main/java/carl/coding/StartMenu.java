package carl.coding;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class StartMenu extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        ImageButton themeToggleButton = findViewById(R.id.themeToggleButton);
        boolean darkModeEnabled = PreferencesHelper.isDarkModeEnabled(this);
        themeToggleButton.setImageResource(darkModeEnabled ? R.drawable.ic_theme_sun : R.drawable.ic_theme_moon);
        themeToggleButton.setContentDescription(darkModeEnabled ? "Switch to light mode" : "Switch to dark mode");
        themeToggleButton.setOnClickListener(v -> {
            PreferencesHelper.setDarkModeEnabled(this, !darkModeEnabled);
            // Changing the default night mode recreates every started activity.
            PreferencesHelper.applyNightMode(this);
        });
    }

    public void goCoin(View view) {
        open(CoinActivity.class);
    }

    public void goDice(View view) {
        open(DiceActivity.class);
    }

    public void goCategories(View view) {
        open(CategoriesSetupActivity.class);
    }

    public void goTeams(View view) {
        open(TeamSetupActivity.class);
    }

    public void goNumbers(View view) {
        open(NumberActivity.class);
    }
}
