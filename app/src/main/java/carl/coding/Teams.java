package carl.coding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Splits players into teams whose sizes differ by at most one. */
final class Teams {

    static final int MIN_TEAMS = 2;

    private Teams() {
    }

    /**
     * Parses the requested team count and clamps it to [2, playerCount]. Empty or
     * unparsable input (including numbers too large for an int) gives the minimum.
     */
    static int teamCount(String input, int playerCount) {
        int requested;
        try {
            requested = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            requested = input.trim().matches("\\d+") ? Integer.MAX_VALUE : MIN_TEAMS;
        }
        return Math.max(MIN_TEAMS, Math.min(requested, playerCount));
    }

    static List<List<String>> split(List<String> players, int teamCount, Random random) {
        List<String> shuffled = new ArrayList<>(players);
        Collections.shuffle(shuffled, random);

        List<List<String>> teams = new ArrayList<>();
        for (int i = 0; i < teamCount; i++) {
            teams.add(new ArrayList<>());
        }
        for (int i = 0; i < shuffled.size(); i++) {
            teams.get(i % teamCount).add(shuffled.get(i));
        }
        return teams;
    }
}
