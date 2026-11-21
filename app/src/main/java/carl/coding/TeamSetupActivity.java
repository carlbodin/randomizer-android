package carl.coding;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class TeamSetupActivity extends BaseActivity {

    private static final String KEY_TEAM_PLAYERS = "team_players";
    private static final String KEY_TEAM_COUNT = "team_count";

    private EditableList players;
    private EditText teamCountInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_setup);

        players = new EditableList(findViewById(R.id.player_list_layout), getString(R.string.teams_player_hint),
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        teamCountInput = findViewById(R.id.team_count_input);

        findViewById(R.id.add_player_button).setOnClickListener(v -> players.addRow(null, true));
        findViewById(R.id.randomize_teams_button).setOnClickListener(v -> startTeamResult());

        teamCountInput.setText(PreferencesHelper.getString(this, KEY_TEAM_COUNT, "2"));
        players.show(PreferencesHelper.getStringList(this, KEY_TEAM_PLAYERS), 4);
    }

    private void startTeamResult() {
        players.removeEmptyRows();
        persistState();

        ArrayList<String> names = players.values();
        if (names.size() < 2) {
            Toast.makeText(this, R.string.teams_need_two, Toast.LENGTH_SHORT).show();
            return;
        }

        int teamCount = Teams.teamCount(teamCountInput.getText().toString(), names.size());

        Intent intent = new Intent(this, TeamResultActivity.class);
        intent.putStringArrayListExtra(TeamResultActivity.EXTRA_PLAYERS, names);
        intent.putExtra(TeamResultActivity.EXTRA_TEAM_COUNT, teamCount);
        startActivity(intent);
    }

    private void persistState() {
        PreferencesHelper.setStringList(this, KEY_TEAM_PLAYERS, players.values());
        PreferencesHelper.setString(this, KEY_TEAM_COUNT, teamCountInput.getText().toString().trim());
    }

    @Override
    protected void onPause() {
        persistState();
        super.onPause();
    }
}
