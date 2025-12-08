package game.playerProfile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class responsible for managing player profiles stored in a plain text file.
 * Profiles are saved in using the format:
 * This class supports loading, saving, adding, and deleting profiles,
 * as well as cleaning up related save files when a profile is removed.
 *
 * @author Alex Samuel
 * @version 1.0
 */
public class ProfileManager {

    /** Path to the text file where profiles are stored. */
    private static final String FILE_PATH = "playerProfiles.txt";

    /**
     * Loads all player profiles from the profile storage file.
     * If the file does not exist, an empty list is returned.
     * Invalid or malformed lines are ignored.
     *
     * @return a list of all valid objects
     */
    public static List<PlayerProfile> loadProfiles() {
        List<PlayerProfile> profiles = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return profiles;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {

                if (!line.isBlank()) {
                    String[] parts = line.split("\\|");

                    if (parts.length == 2) {
                        String name = parts[0];
                        int unlockedLevel = 1;

                        try {
                            unlockedLevel = Integer.parseInt(parts[1]);
                        } catch (NumberFormatException ignored) {
                            // leave unlockedLevel as default 1
                        }

                        profiles.add(new PlayerProfile(name, unlockedLevel));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return profiles;
    }

    /**
     * Saves the provided list of profiles to the storage file.
     * Existing file contents are overwritten. Each profile is written on a separate line
     * in the format.
     *
     * @param profiles the list of profiles to persist
     */
    public static void saveProfiles(List<PlayerProfile> profiles) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (PlayerProfile p : profiles) {
                writer.println(p.getName() + "|" + p.getMaxUnlockedLevel());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new profile to persistent storage.
     * This method loads all existing profiles, appends the new one,
     * and rewrites the profile file.
     *
     * @param profile the new profile to add
     */
    public static void addProfile(PlayerProfile profile) {
        List<PlayerProfile> list = loadProfiles();
        list.add(profile);
        saveProfiles(list);
    }

    /**
     * Deletes a profile from storage and removes all save files
     * associated with that profile.
     * The method removes the matching profile entry from {@code playerProfiles.txt}
     * and deletes any files inside the {@code saves} directory whose names begin
     * with the profile's name.
     *
     * @param profile the profile to remove
     */
    public static void deleteProfile(PlayerProfile profile) {
        List<PlayerProfile> list = loadProfiles();
        list.removeIf(p -> p.getName().equalsIgnoreCase(profile.getName()));
        saveProfiles(list);

        File saveFolder = new File("saves");
        if (saveFolder.exists() && saveFolder.isDirectory()) {
            File[] files = saveFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().startsWith(profile.getName())) {
                        boolean deleted = file.delete();
                        if (!deleted) {
                            System.out.println("Failed to delete save file: " + file.getName());
                        }
                    }
                }
            }
        }
    }
}
