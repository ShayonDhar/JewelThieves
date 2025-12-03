package game;

/**
 * Represents a temporary visual explosion effect at a specific tile location.
 * Stores the tile coordinates and the time at which the effect should expire.
 * The rendering system uses this data to draw the explosion until it ends.
 *
 * @author Alex Samuel.
 * @version 1.0
 */

public class ExplosionEffect {
    protected int x;
    protected int y;
    protected long endTime;

    /**
     * Explosion effect constructor.
     *
     * @param x the x coordinate of the bomb
     * @param y the y coordinate of the bomb
     * @param durationMs the length of time of the bom
     */

    public ExplosionEffect(int x, int y, long durationMs) {
        this.x = x;
        this.y = y;
        this.endTime = System.currentTimeMillis() + durationMs;
    }
}

