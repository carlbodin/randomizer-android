package carl.coding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TeamsTest {

    @Test
    public void teamCountIsClampedToPlayers() {
        assertEquals(2, Teams.teamCount("", 5));
        assertEquals(2, Teams.teamCount("0", 5));
        assertEquals(2, Teams.teamCount("1", 5));
        assertEquals(3, Teams.teamCount(" 3 ", 5));
        assertEquals(5, Teams.teamCount("9", 5));
        assertEquals(5, Teams.teamCount("99999999999", 5));
    }

    @Test
    public void splitKeepsEveryPlayerInEvenTeams() {
        List<String> players = Arrays.asList("A", "B", "C", "D", "E", "F", "G");
        List<List<String>> teams = Teams.split(players, 3, new Random(1));

        assertEquals(3, teams.size());
        List<String> all = new ArrayList<>();
        for (List<String> team : teams) {
            assertTrue(team.size() == 2 || team.size() == 3);
            all.addAll(team);
        }
        assertEquals(players.size(), all.size());
        assertTrue(all.containsAll(players));
    }
}
