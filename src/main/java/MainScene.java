import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MainScene {

    public static Scene getScene() {
        Text t = new Text();
        t.setFont(new Font(18));
        t.setText("Welcome to Boggle!");

        Button play = new Button("Play!");
        play.setDefaultButton(true);
        play.setOnMouseClicked((e) -> {
            BoggleGUI.stage.setScene(SetupScene.getScene());
        });
        Button settings = new Button("Settings");
        settings.setAlignment(Pos.BOTTOM_RIGHT);

        VBox pane = new VBox(t, new Text(), play, new Text(), settings);
        pane.setAlignment(Pos.CENTER);
        return new Scene(pane, 256, 256);
    }

}
