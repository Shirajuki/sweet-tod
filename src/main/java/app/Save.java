package app;

import java.util.List;

/* An interface with methods for saving and loading player file and leaderboard. */
public interface Save {
    public List<String> loadChar();

    public void saveChar(List<String> ls);

    public List<String> listedLeaderboard();

    public void writeLeaderboard(List<String> ls);
}
