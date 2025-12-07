package game.playerProfile;

public class ProfileSession {
    private static PlayerProfile current;

    public static void set(PlayerProfile player) { current = player; }

    public static PlayerProfile getProfile() { return current; }
    public static String getCurrentName() { return current.getName(); }
}

