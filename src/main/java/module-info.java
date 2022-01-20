
// No sense in this file.
// We put what was needed to make the project compile
module com.App {
    requires transitive common;
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.Tetris.Net.Updates;
    opens com.Tetris.Net.Updates.UpdateDataTypes;

    exports com.App;
    exports com.Tetris.View;
    exports com.Tetris.Controller;
    exports com.Tetris.Net;
    exports com.Tetris.Net.Updates;
    exports com.Tetris.Net.Updates.UpdateDataTypes;
    exports com.Tetris.Model;
    exports com.Tetris.Model.Opponent;
    exports com.Tetris.Model.Generators;
    exports com.Tetris.Model.Tetriminos;
    exports com.Tetris.Model.Tetriminos.FallingTetriminos;
}