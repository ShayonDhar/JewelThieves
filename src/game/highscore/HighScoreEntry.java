package game.highscore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a single entry in a high score table.
 * Each entry contains a player's name, their score, and the timestamp
 * when the score was achieved. Entries are comparable based on score
 * (higher is better) and timestamp (earlier is better for tie-breaking).
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class HighScoreEntry implements Comparable<HighScoreEntry> {

    public static final String SPLIT_REGEX = "\\|";
    public static final String TO_STRING_FORMAT = "%s: %d";
    // The date-time formatter used for serializing timestamps to file format.
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final int PART_LENGTH = 3;
    private static final String INVALID_SCORE_FORMAT = "Invalid high score entry format: ";
    private static final String FILE_STRING_FORMAT = "%s|%d|%s";
    private final String playerName;
    private final int score;
    private final LocalDateTime timestamp;

    /**
     * Creates a new high score entry with the current timestamp.
     *
     * @param playerName the name of the player
     * @param score the score achieved by the player
     */
    public HighScoreEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Creates a new high score entry with a specified timestamp.
     * This constructor is used when loading entries from a file.
     *
     * @param playerName the name of the player
     * @param score the score achieved by the player
     * @param timestamp the timestamp when the score was achieved
     * @throws NullPointerException if playerName or timestamp is null
     */
    public HighScoreEntry(String playerName, int score, LocalDateTime timestamp) {
        this.playerName = playerName;
        this.score = score;
        this.timestamp = timestamp;
    }

    /**
     * Gets the name of the player.
     *
     * @return player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Gets the time stamp.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the score value.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Compares this entry with another for ordering.
     * Entries are ordered by score (descending) first, then by timestamp
     * (ascending) for tie-breaking. This means higher scores come first,
     * and for equal scores, earlier timestamps come first.
     *
     * @param other the entry to compare to
     * @return a negative integer, zero, or a positive integer as this entry
     *         is greater than, equal to, or less than the specified entry
     */
    @Override
    public int compareTo(HighScoreEntry other) {
        int scoreComparison = Integer.compare(other.score, this.score);
        if (scoreComparison != 0) {
            return scoreComparison;
        }
        return this.timestamp.compareTo(other.timestamp);
    }

    /**
     * Indicates whether some other object is equal to this one.
     * Two HighScoreEntry objects are considered equal if they have the same
     * player name, score, and timestamp.
     *
     * @param obj the reference object with which to compare
     * @return true if this object is equal to the obj argument
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        HighScoreEntry other = (HighScoreEntry) obj;
        return score == other.score
                && Objects.equals(playerName, other.playerName)
                && Objects.equals(timestamp, other.timestamp);
    }

    /**
     * Returns a hash code value for this entry.
     * The hash code is computed based on the player name, score, and timestamp.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(playerName, score, timestamp);
    }

    /**
     * Converts this entry to a file format string.
     *
     * @return a string representation for file storage
     */
    public String toFileString() {
        return String.format(FILE_STRING_FORMAT,
                playerName,
                score,
                timestamp.format(FORMATTER));
    }

    /**
     * Creates a HighScoreEntry from a file format string.
     *
     * @param line the file format string to parse
     * @return a new HighScoreEntry parsed from the string
     * @throws IllegalArgumentException if the line format is invalid
     */
    public static HighScoreEntry fromFileString(String line) {
        String[] parts = line.split(SPLIT_REGEX);
        if (parts.length != PART_LENGTH) {
            throw new IllegalArgumentException(INVALID_SCORE_FORMAT + line);
        }

        String playerName = parts[0];
        int score = Integer.parseInt(parts[1]);
        LocalDateTime timestamp = LocalDateTime.parse(parts[2], FORMATTER);

        return new HighScoreEntry(playerName, score, timestamp);
    }

    /**
     * Returns a string representation of this entry.
     *
     * @return a string representation of this entry
     */
    @Override
    public String toString() {
        return String.format(TO_STRING_FORMAT, playerName, score);
    }
}
