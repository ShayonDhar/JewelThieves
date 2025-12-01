package game.entity.npc;

import game.entity.Direction;
import game.entity.EntityName;
import game.level.Level;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.canvas.GraphicsContext;


/*
SmartThief implementation:
Most intelligent NPC, potentially hard to code.
- Collect as much loot as possible then attempt to reach
an exit before the player does.
*/

public class SmartThief extends NPC {
    private static final int SPRITE_SIZE = 35;
    /**
     * Constructor to create the Smart Thief.
     *
     * @param x              x coordinate of the Smart Thief
     * @param y              y coordinate of the Smart Thief
     * @param direction      direction the Smart Thief is facing
     * @param alive          the alive state of the Smart Thief
     * @param blocksMovement whether the SmartThief blocks movement of other entities
     */
    public SmartThief(int x, int y, Direction direction, boolean alive, boolean blocksMovement, Level level) {
        super(EntityName.SMART_THIEF, x, y, direction, alive, blocksMovement, level);

        sprite = new ImageView(
                new Image(game.entity.Player.class.getResource("/game/resources/smartthief.png").toExternalForm())
        );
        sprite.setFitWidth(SPRITE_SIZE);
        sprite.setFitHeight(SPRITE_SIZE);
    }



    @Override
    public void addToHighscore(int value) {
        
    }

    @Override
    public void move()
    {
        //Implemented in Level!
    }

}


