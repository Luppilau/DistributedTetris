module com.App {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires common;

    exports com.App;
    exports com.Tetris.Model;
    exports com.Tetris.View;
    exports com.Tetris.Net;
}