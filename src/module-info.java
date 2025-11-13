module JewelThieves {
    requires javafx.graphics;
    requires javafx.controls;

    opens game to  javafx.graphics, javafx.fxml;
    exports game;
    exports game.entity;
    opens game.entity to javafx.fxml, javafx.graphics;

}