module JewelThieves {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens game to  javafx.graphics, javafx.fxml;
    exports game;
    exports game.entity;
    opens game.entity to javafx.fxml, javafx.graphics;

}