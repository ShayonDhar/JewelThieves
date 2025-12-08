package game.highscore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a single entry (row) in a high score leaderboard.
 * All fields are immutable in terms of reference after construction (except through property setters),
 * but the property values themselves can be updated.
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class ScoreRow {

    /**
     * Date format pattern used for displaying the achievement timestamp.
     */
    private static final String DATE_FORMAT_PATTERN = "dd MMM yyyy";
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
    private static final String PLAYER_NAME_NOT_NULL = "playerName cannot be null";
    private static final String DATE_NOT_NULL = "date cannot be null";
    private static final String TIMESTAMP_NULL = "timestamp cannot be null";

    private final IntegerProperty rank = new SimpleIntegerProperty(this, "rank");
    private final StringProperty playerName = new SimpleStringProperty(this, "playerName");
    private final IntegerProperty score = new SimpleIntegerProperty(this, "score");
    private final StringProperty date = new SimpleStringProperty(this, "date");

    /**
     * Creates a new ScoreRow with the specified values.
     *
     * @param rank       the current rank of the player (1 = highest)
     * @param playerName the name of the player (cannot be null)
     * @param score      the achieved score
     * @param date       the formatted date string
     * @throws NullPointerException if playerName or date is null
     */
    public ScoreRow(int rank, String playerName, int score, String date) {
        this.rank.set(rank);
        this.playerName.set(Objects.requireNonNull(playerName, PLAYER_NAME_NOT_NULL));
        this.score.set(score);
        this.date.set(Objects.requireNonNull(date, DATE_NOT_NULL));
    }

    /**
     * Factory method to create a ScoreRow from a LocalDateTime timestamp.
     * The date will be formatted using the pattern
     *
     * @param rank       the current rank (1-based)
     * @param playerName the player's name (must not be null)
     * @param score      the achieved score
     * @param timestamp  the exact moment the score was achieved (must not be null)
     * @return a new {@code ScoreRow} instance with formatted date
     * @throws NullPointerException if any argument except {@code rank} or {@code score} is null
     */
    public static ScoreRow of(int rank, String playerName, int score, LocalDateTime timestamp) {
        Objects.requireNonNull(timestamp, TIMESTAMP_NULL);
        String formattedDate = timestamp.format(DATE_FORMATTER);
        return new ScoreRow(rank, playerName, score, formattedDate);
    }

    /**
     * Returns the property holding the player's current rank.
     *
     * @return the rank property
     */
    public IntegerProperty rankProperty() {
        return rank;
    }

    /**
     * Returns the property holding the player's name.
     *
     * @return the player name property
     */
    public StringProperty playerNameProperty() {
        return playerName;
    }

    /**
     * Returns the property holding the achieved score.
     *
     * @return the score property
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Returns the property holding the formatted date string.
     *
     * @return the date property
     */
    public StringProperty dateProperty() {
        return date;
    }

    /**
     * Returns the current rank value.
     *
     * @return the rank (1 = highest)
     */
    public int getRank() {
        return rank.get();
    }

    /**
     * Returns the player's name.
     *
     * @return the player name, never null
     */
    public String getPlayerName() {
        return playerName.get();
    }

    /**
     * Returns the achieved score.
     *
     * @return the score value
     */
    public int getScore() {
        return score.get();
    }

    /**
     * Returns the formatted date string (e.g., "08 Dec 2025").
     *
     * @return the formatted date, never null
     */
    public String getDate() {
        return date.get();
    }

    /**
     * Updates the rank of this entry. Useful when the leaderboard is re-sorted.
     *
     * @param rank the new rank (typically 1-based)
     */
    public void setRank(int rank) {
        this.rank.set(rank);
    }

    /**
     * Updates the player name.
     *
     * @param playerName the new name (must not be null)
     * @throws NullPointerException if {@code playerName} is null
     */
    public void setPlayerName(String playerName) {
        this.playerName.set(Objects.requireNonNull(playerName, PLAYER_NAME_NOT_NULL));
    }

    /**
     * Updates the score value.
     *
     * @param score the new score
     */
    public void setScore(int score) {
        this.score.set(score);
    }

    /**
     * Updates the date string.
     *
     * @param date the new formatted date (must not be null)
     * @throws NullPointerException if date is null
     */
    public void setDate(String date) {
        this.date.set(Objects.requireNonNull(date, DATE_NOT_NULL));
    }

    @Override
    public String toString() {
        return "ScoreRow{"
                + "rank="
                + rank
                + ", playerName="
                + playerName
                + ", score="
                + score + ", date=" + date
                + '}';
    }
}
