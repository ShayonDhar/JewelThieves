package game.highscore;

import java.io.*;
import java.util.*;

/**
 * Manages the high score table for a single game level.
 * This class maintains a sorted list of the top 10 high scores for a specific
 * level. It provides methods to add new scores, query rankings, and persist
 * the high score data to and from text files.
 * The high score table automatically maintains sorted order with the highest
 * scores at the top. Only the top 10 scores are retained.
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class LevelHighScoreTable {
    /**
     * The maximum number of entries to keep in the high score table.
     */
    private static final int MAX_ENTRIES = 10;
    private static final String LEVEL = "LEVEL:";
    private static final String INVALID_FILE_FORMAT = "Invalid high score file format";
    private static final String LEVEL_MISMATCH = "Level mismatch: expected ";
    private static final String SKIPPING_INVALID_ENTRY = "Skipping invalid entry: ";
    private static final String HIGH_SCORES = " High Scores:\n";
    private static final String LEVEL_TO_STRING = "Level ";
    private static final String NO_SCORES_YET = "No scores yet.\n";
    private static final String TO_STRING_FORMAT = "%2d. %s\n";
    public static final int REPEAT_DISPLAY = 40;

    /**
     * The level number this high score table represents.
     */
    private final int levelNumber;
    private final List<HighScoreEntry> entries;

    /**
     * Creates a new high score table for the specified level.
     *
     * @param levelNumber the level number
     */
    public LevelHighScoreTable(int levelNumber) {
        this.levelNumber = levelNumber;
        this.entries = new ArrayList<>();
    }

    /**
     * Attempts to add a new score to the high score table.
     * The score is only added if it qualifies for the top 10. If the table
     * is full and the new score is lower than all existing scores, it will
     * not be added.
     *
     * @param playerName the name of the player
     * @param score the score to add
     * @return true if the score was added to the table, else return false
     * @throws NullPointerException if playerName is null
     */
    public boolean addScore(String playerName, int score) {
        HighScoreEntry newEntry = new HighScoreEntry(playerName, score);

        if (entries.size() < MAX_ENTRIES ||
                newEntry.compareTo(entries.get(entries.size() - 1)) < 0) {

            entries.add(newEntry);
            Collections.sort(entries);

            if (entries.size() > MAX_ENTRIES) {
                entries.remove(MAX_ENTRIES);
            }

            return true;
        }

        return false;
    }

    /**
     * Returns an unmodifiable view of the high scores.
     * The returned list is sorted in descending order by score.
     *
     * @return an unmodifiable list of high score entries
     */
    public List<HighScoreEntry> getHighScores() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Checks if a score would qualify for the high score table.
     * A score qualifies if the table has fewer than 10 entries, or if
     * the score is higher than the lowest score currently in the table.
     *
     * @param score the score to check
     * @return true if the score would be added to the table,
     */
    public boolean wouldQualify(int score) {
        if (entries.size() < MAX_ENTRIES) {
            return true;
        }
        return score > entries.get(entries.size() - 1).getScore();
    }

    /**
     * Returns the rank a score would achieve in the high score table.
     *
     * @param score the score to check
     * @return the rank (1-10) the score would achieve, or -1 if the score
     *         would not qualify for the table
     */
    public int getRankForScore(int score) {
        if (!wouldQualify(score)) {
            return -1;
        }

        for (int i = 0; i < entries.size(); i++) {
            if (score > entries.get(i).getScore()) {
                return i + 1;
            }
        }
        return entries.size() + 1;
    }

    /**
     * Clears all entries from this high score table.
     */
    public void clear() {
        entries.clear();
    }

    /**
     * Returns the number of entries currently in the high score table.
     *
     * @return the number of entries (0-10)
     */
    public int size() {
        return entries.size();
    }

    /**
     * Saves this high score table to a text file.
     *
     * @param filePath the path to the file where scores should be saved
     * @throws IOException if an I/O error occurs during saving
     */
    public void saveToFile(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header with level number
            writer.write(LEVEL + levelNumber);
            writer.newLine();

            // Write each entry
            for (HighScoreEntry entry : entries) {
                writer.write(entry.toFileString());
                writer.newLine();
            }
        }
    }

    /**
     * Loads high scores from a text file into this table.
     * Any existing scores in the table are cleared before loading.
     * If the file does not exist, the table is simply cleared and no
     * error is thrown.
     *
     * @param filePath the path to the file to load scores from
     * @throws IOException if an I/O error occurs or the file format is invalid
     * @throws NumberFormatException if score values cannot be parsed
     */
    public void loadFromFile(String filePath) throws IOException {
        entries.clear();

        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();

            // Read and validate header
            if (line == null || !line.startsWith(LEVEL)) {
                throw new IOException(INVALID_FILE_FORMAT);
            }

            int fileLevelNumber = Integer.parseInt(line.substring(6));
            if (fileLevelNumber != levelNumber) {
                throw new IOException(LEVEL_MISMATCH + levelNumber
                        + ", found " + fileLevelNumber);
            }

            // Read entries
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        HighScoreEntry entry = HighScoreEntry.fromFileString(line);
                        entries.add(entry);
                    } catch (Exception e) {
                        System.out.println(SKIPPING_INVALID_ENTRY + line);
                    }
                }
            }

            // Ensure entries are sorted
            Collections.sort(entries);
        }
    }

    /**
     * Returns a formatted string representation of this high score table.
     * The output includes a header with the level number and a numbered
     * list of all high scores.
     *
     * @return a formatted string showing all high scores for this level
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(LEVEL_TO_STRING).append(levelNumber).append(HIGH_SCORES);
        sb.append("=".repeat(REPEAT_DISPLAY)).append("\n");

        for (int i = 0; i < entries.size(); i++) {
            sb.append(String.format(TO_STRING_FORMAT, i + 1, entries.get(i)));
        }

        if (entries.isEmpty()) {
            sb.append(NO_SCORES_YET);
        }

        return sb.toString();
    }
}