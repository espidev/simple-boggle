import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SetupScene {
    public static Scene getScene() {
        BoggleGUI.stage.setTitle("Boggle Setup");

        Text mode = new Text();
        mode.setText("Which mode are you playing?");
        Button single = new Button("Singleplayer"), multi = new Button("2 Players");

        multi.setOnMouseClicked(e -> Boggle.startGame());

        VBox pane = new VBox(mode, new Text(), single, new Text(), multi);
        pane.setAlignment(Pos.CENTER);
        return new Scene(pane, 256, 256);
    }
    public static Scene setupPlayers() {
        VBox pane = new VBox();

        pane.setAlignment(Pos.CENTER);
        return new Scene(pane, 256, 256);
    }
}
