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
    exports com.Tetris.Model.Tetriminos;
    exports com.Tetris.Controller;
    exports com.Tetris.Model.Tetriminos.FallingPieces;
    exports com.Tetris.Model.Generators;
    exports com.Tetris.Net.Updates.UpdateDataTypes;
    opens com.Tetris.Net.Updates.UpdateDataTypes;
}