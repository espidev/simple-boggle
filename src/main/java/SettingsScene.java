import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class SettingsScene {
    public static Scene getScene() {
        BoggleGUI.stage.setTitle("Settings");
        GridPane pane = new GridPane();

        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setVgap(5);
        pane.setHgap(5);

        pane.add(new Text("Points to play:"), 0, 0);
        pane.add(new Text("Number of players:"), 1, 0);
        pane.add(new Text("Minimum word length:"), 2, 0);

        Button save = new Button();

        return new Scene(pane, 256, 256);
    }
}
