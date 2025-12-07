package game.highscore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    /**
     * The date-time formatter used for serializing timestamps to file format.
     */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final int PART_LENGTH = 3;
    private static final String INVALID_SCORE_FORMAT = "Invalid high score entry format: ";
    private static final String FILE_STRING_FORMAT = "%s|%d|%s";

    /**
     * The name of the player who achieved this score.
     */
    private final String playerName;

    /**
     * The score value achieved by the player.
     */
    private final int score;

    /**
     * The timestamp when this score was achieved.
     */
    private final LocalDateTime timestamp;

    /**
     * Creates a new high score entry with the current timestamp.
     *
     * @param playerName the name of the player (must not be null)
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
     * @param playerName the name of the player (must not be null)
     * @param score the score achieved by the player
     * @param timestamp the timestamp when the score was achieved (must not be null)
     * @throws NullPointerException if playerName or timestamp is null
     */
    public HighScoreEntry(String playerName, int score, LocalDateTime timestamp) {
        this.playerName = playerName;
        this.score = score;
        this.timestamp = timestamp;
    }

    /**
     * Gets the player's name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
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
     * Gets the timestamp when this score was achieved.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
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
        String[] parts = line.split("\\|");
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
        return String.format("%s: %d", playerName, score);
    }
}

