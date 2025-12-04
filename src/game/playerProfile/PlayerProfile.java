package game.playerProfile;

public class PlayerProfile {

    private String name;
    private int maxUnlockedLevel;

    public PlayerProfile(String name) {
        this(name, 1);
    }

    public PlayerProfile(String name, int maxUnlockedLevel) {
        this.name = name.trim();
        this.maxUnlockedLevel = Math.max(1, maxUnlockedLevel);
    }

    public String getName() {
        return name;
    }

    public int getMaxUnlockedLevel() {
        return maxUnlockedLevel;
    }

    public void setMaxUnlockedLevel(int level) {
        this.maxUnlockedLevel = Math.max(1, level);
    }

    @Override
    public String toString() {
        return name;
    }
}
