package carl.coding;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeamResultActivity extends BaseActivity {

    static final String EXTRA_PLAYERS = "players";
    static final String EXTRA_TEAM_COUNT = "teamCount";

    private final Random random = new Random();
    private ArrayList<String> players;
    private int teamCount;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_result);

        players = getIntent().getStringArrayListExtra(EXTRA_PLAYERS);
        teamCount = getIntent().getIntExtra(EXTRA_TEAM_COUNT, Teams.MIN_TEAMS);
        resultText = findViewById(R.id.team_result_text);
        Button rerollButton = findViewById(R.id.reroll_teams_button);

        rerollButton.setOnClickListener(v -> renderTeams());
        if (savedInstanceState == null) {
            renderTeams();
        }
    }

    private void renderTeams() {
        if (players == null || players.isEmpty()) {
            resultText.setText("No players available.");
            return;
        }

        List<List<String>> teams = Teams.split(players, teamCount, random);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < teams.size(); i++) {
            if (i > 0) {
                builder.append("\n\n");
            }
            builder.append("Team ").append(i + 1);
            for (String player : teams.get(i)) {
                builder.append("\n• ").append(player);
            }
        }
        resultText.setText(builder);
        vibrate(180);
    }
}
