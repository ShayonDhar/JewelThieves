module JewelThieves {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens game to  javafx.graphics, javafx.fxml;
    exports game;
    exports game.entity;
    opens game.entity to javafx.fxml, javafx.graphics;
    exports game.entity.npc;
    opens game.entity.npc to javafx.fxml, javafx.graphics;
    exports game.level;
    opens game.level to javafx.fxml, javafx.graphics;
    exports game.item;
    opens game.item to javafx.fxml, javafx.graphics;
    exports game.playerProfile;
    opens game.playerProfile to javafx.fxml, javafx.graphics;
    exports game.save to javafx.fxml, javafx.graphics;
    opens game.save to javafx.fxml, javafx.graphics;
    opens game.highscore to javafx.fxml, javafx.base;

}