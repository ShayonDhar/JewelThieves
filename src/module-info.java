module JewelThieves {
    requires javafx.graphics;
    requires javafx.controls;

    opens main to  javafx.graphics, javafx.fxml;
    exports main;

}