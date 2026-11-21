package carl.coding;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Target of the launcher aliases. Android removes a task when the component that
 * started it is disabled, so the app must not run in the alias's task: this opens
 * StartMenu in the app's own task (or brings that task back) and finishes at once.
 */
@SuppressLint("CustomSplashScreen") // Shows nothing; it only forwards to StartMenu.
public class LaunchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, StartMenu.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}
