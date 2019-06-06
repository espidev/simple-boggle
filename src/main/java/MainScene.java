//
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MainScene {

    public static Scene getScene() {
        BoggleGUI.stage.setTitle("Boggle Game");

        Text t = new Text();
        t.setFont(new Font(18));
        t.setText("Welcome to Boggle!");

        Button play = new Button("Play!"), settings = new Button("Settings");
        play.setDefaultButton(true);
        play.setOnMouseClicked((e) -> {
            SetupScene.currentPlayer = 0;
            BoggleGUI.stage.setScene(SetupScene.getScene());
        });
        settings.setOnMouseClicked(e -> BoggleGUI.stage.setScene(SettingsScene.getScene()));

        VBox pane = new VBox(t, new Text(), play, new Text(), settings);
        pane.setAlignment(Pos.CENTER);
        return new Scene(pane, 256, 256);
    }

}
