package game.entity.npc;

import game.entity.Direction;
import game.entity.EntityName;
import game.entity.Player;
import game.level.Level;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/*
SmartThief implementation:
Most intelligent NPC, potentially hard to code.
- Collect as much loot as possible then attempt to reach
an exit before the player does.
*/

/**
 * Smart thief class which stores all the smart thief
 * details such as its current coordinates, direction
 * whether it's alive or not and whether it blocks movement
 * of other entities.
 *
 * @author Keyan Jaf
 * @version 1.0.0
 */

public class SmartThief extends NPC {
    private static final int SPRITE_SIZE = 35;
    private static final String SMARTTHIEF_PNG = "/game/resources/smartthief.png";

    /**
     * Constructor to create the Smart Thief.
     *
     * @param x              x coordinate of the Smart Thief
     * @param y              y coordinate of the Smart Thief
     * @param direction      direction the Smart Thief is facing
     * @param alive          the alive state of the Smart Thief
     * @param blocksMovement whether the SmartThief blocks movement of other entities
     * @param level the level the smart thief is currently on.
     */

    public SmartThief(int x, int y, Direction direction, boolean alive, boolean blocksMovement, Level level) {
        super(EntityName.SMART_THIEF, x, y, direction, alive, blocksMovement, level);

        sprite = new ImageView(
                new Image(Objects.requireNonNull(Player.class.getResource(SMARTTHIEF_PNG)).toExternalForm())
        );
        sprite.setFitWidth(SPRITE_SIZE);
        sprite.setFitHeight(SPRITE_SIZE);
    }

    /**
     * Adds the score to the highscore table.
     *
     * @param value value to be added to High score
     */
    @Override
    public void addToHighscore(int value) {
        
    }

    @Override
    public void move() {
        // Implemented in Level!
    }

}


