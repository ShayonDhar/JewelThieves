package game.highscore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScoreRow {
    private final int rank;
    private final String playerName;
    private final int score;
    private final String date;

    public ScoreRow(int rank, String playerName, int score, String date) {
        this.rank = rank;
        this.playerName = playerName;
        this.score = score;
        this.date = date;
    }

    public int getRank() {
        return rank;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }

    public static ScoreRow of(int rank, String playerName, int score, LocalDateTime timestamp) {
        String formattedDate = timestamp.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        return new ScoreRow(rank, playerName, score, formattedDate);
    }
}
