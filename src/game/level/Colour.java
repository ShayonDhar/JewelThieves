package game.level;

import javafx.scene.paint.Color;

public enum Colour {
    RED('R', Color.RED),
    GREEN('G', Color.GREEN),
    BLUE('B', Color.BLUE),
    YELLOW('Y', Color.YELLOW),
    CYAN('C', Color.CYAN),
    MAGENTA('M', Color.MAGENTA);

    private final char code;
    private final Color fxColor;

    Colour(char code, Color fxColor) {
        this.code = code;
        this.fxColor = fxColor;
    }

    public static Colour fromChar(char c) {
        return switch (c) {
            case 'R' -> RED;
            case 'G' -> GREEN;
            case 'B' -> BLUE;
            case 'Y' -> YELLOW;
            default -> throw new IllegalArgumentException("Unknown colour: " + c);
        };
    }

    public char getCode() {
        return code;
    }

    public Color getFxColor() {
        return fxColor;
    }

    public static Colour fromCode(char c) {
        for (Colour col : values()) {
            if (col.code == c) return col;
        }
        throw new IllegalArgumentException("Invalid colour code: " + c);
    }
}
