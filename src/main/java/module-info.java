module com.App {
    requires transitive common;
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.Tetris.Net.Updates;

    exports com.App;
    exports com.Tetris.Model;
    exports com.Tetris.Model.Opponent;
    exports com.Tetris.View;
    exports com.Tetris.Net;
    exports com.Tetris.Net.Updates;
}