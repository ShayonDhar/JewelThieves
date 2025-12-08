package game.level;

import javafx.scene.paint.Color;

/**
 * Defines the colours of each level.
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */

public enum Colour {
    RED('R', Color.web("#cb0404")),
    GREEN('G', Color.web("#8ec127")),
    BLUE('B', Color.web("#017cf3")),
    YELLOW('Y', Color.web("#ff9f00")),
    CYAN('C', Color.web("#00e5e5")),
    MAGENTA('M', Color.web("#640d5f"));

    private final char code;
    private final Color fxColor;

    Colour(char code, Color fxColor) {
        this.code = code;
        this.fxColor = fxColor;
    }

    /**
     * Defines the characters with their colours
     * the file represents colours as characters.
     *
     * @param c the colour character
     * @return the colour of the character
     */
    public static Colour fromChar(char c) {
        return switch (c) {
            case 'R' -> RED;
            case 'G' -> GREEN;
            case 'B' -> BLUE;
            case 'Y' -> YELLOW;
            case 'C' -> CYAN;
            case 'M' -> MAGENTA;
            default -> throw new IllegalArgumentException("Unknown colour: " + c);
        };
    }

    /**
     *Takes a JavaFX Color and tries to find the corresponding Colour enum constant.
     * If it doesn’t find one, it defaults to CYAN.
     *
     * @param color The enum colour
     * @return the corresponding JavaFX colour
     */
    public static Colour fromFXColor(Color color) {
        for (Colour c : values()) {
            if (c.getFXColor().equals(color)) {
                return c;
            }
        }
        return CYAN; // default fallback
    }

    public char getCode() {
        return code;
    }

    /**
     * Gets the JavaFx colour from the fromFXColour method.
     *
     * @return the fx colour
     */
    public Color getFXColor() {
        return fxColor;
    }

    /**
     *looks up a Colour enum constant based on a character code.
     * If the character doesn’t correspond to any enum constant, it throws an error instead of returning a default.
     *
     * @param c the colour character
     * @return An error or the colour
     */
    public static Colour fromCode(char c) {
        for (Colour col : values()) {
            if (col.code == c) {
                return col;
            }
        }
        throw new IllegalArgumentException("Invalid colour code: " + c);
    }
}
