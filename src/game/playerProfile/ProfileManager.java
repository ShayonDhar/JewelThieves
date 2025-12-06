package game.playerProfile;

import javafx.event.ActionEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileManager {

    private static final String FILE_PATH = "playerProfiles.txt";

    // Load all profiles from the plain text file
    public static List<PlayerProfile> loadProfiles() {
        List<PlayerProfile> profiles = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return profiles;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split("\\|");
                if (parts.length != 2) continue;

                String name = parts[0];
                int unlockedLevel = 1;

                try {
                    unlockedLevel = Integer.parseInt(parts[1]);
                } catch (NumberFormatException ignored) {}

                profiles.add(new PlayerProfile(name, unlockedLevel));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return profiles;
    }

    // Write all profiles to the text file
    public static void saveProfiles(List<PlayerProfile> profiles) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (PlayerProfile p : profiles) {
                writer.println(p.getName() + "|" + p.getMaxUnlockedLevel());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add a new profile
    public static void addProfile(PlayerProfile profile) {
        List<PlayerProfile> list = loadProfiles();
        list.add(profile);
        saveProfiles(list);
    }

    // Delete a profile by name
    public static void deleteProfile(PlayerProfile profile) {
        List<PlayerProfile> list = loadProfiles();
        list.removeIf(p -> p.getName().equalsIgnoreCase(profile.getName()));
        saveProfiles(list);

        
        File saveFolder = new File("saves");
        if (saveFolder.exists() && saveFolder.isDirectory()) {
            File[] files = saveFolder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().startsWith(profile.getName())) {
                        boolean deleted = f.delete();
                        if (!deleted) {
                            System.out.println("Failed to delete save file: " + f.getName());
                        }
                    }
                }
            }
        }
    }

}
