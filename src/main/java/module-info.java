module com.App {
    requires javafx.controls;
    requires javafx.graphics;
    requires common;

    exports com.App;
    exports com.Tetris.Model;
    exports com.Tetris.TetrisCanvas;
}