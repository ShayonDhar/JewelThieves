package game.highscore;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a row in the leaderboard table with JavaFX properties
 * for proper table binding and updates.
 */
public class ScoreRow {
    private final IntegerProperty rank;
    private final StringProperty playerName;
    private final IntegerProperty score;
    private final StringProperty date;

    public ScoreRow(int rank, String playerName, int score, String date) {
        this.rank = new SimpleIntegerProperty(rank);
        this.playerName = new SimpleStringProperty(playerName);
        this.score = new SimpleIntegerProperty(score);
        this.date = new SimpleStringProperty(date);
    }

    public IntegerProperty rankProperty() {
        return rank;
    }

    public StringProperty playerNameProperty() {
        return playerName;
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public int getRank() {
        return rank.get();
    }

    public String getPlayerName() {
        return playerName.get();
    }

    public int getScore() {
        return score.get();
    }

    public String getDate() {
        return date.get();
    }

    public void setRank(int rank) {
        this.rank.set(rank);
    }

    public void setPlayerName(String playerName) {
        this.playerName.set(playerName);
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public static ScoreRow of(int rank, String playerName, int score, LocalDateTime timestamp) {
        String formattedDate = timestamp.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        return new ScoreRow(rank, playerName, score, formattedDate);
    }
}