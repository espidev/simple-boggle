// creates main scene for boggle GUI
// using javaFX
import javafx.geometry.Pos;                                                                         // importing required libraries
import javafx.scene.Scene;                                                                          // importing required libraries
import javafx.scene.control.Button;                                                                 // importing required libraries
import javafx.scene.layout.VBox;                                                                    // importing required libraries
import javafx.scene.text.Font;                                                                      // importing required libraries
import javafx.scene.text.Text;                                                                      // importing required libraries

public class MainScene {

    public static Scene getScene() {
        BoggleGUI.stage.setTitle("Boggle Game");                                                    // naming the window Boggle Game

        VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);

        Text t = new Text("Welcome to Boggle!");                                                                        // creating text box

        Button play = new Button("Play!"), settings = new Button("Settings");                       // creating 2 buttons
        play.setOnAction(e -> {
            SetupScene.currentPlayer = 0;                                                           // starts the setup with the first player
            BoggleGUI.stage.setScene(SetupScene.getScene());
        });
        play.setDefaultButton(true);
        settings.setOnAction(e -> BoggleGUI.stage.setScene(SettingsScene.getScene()));        // change scene if Settings button is pressed

        pane.getChildren().addAll(t, new Text(), play, new Text(), settings);

        BoggleGUI.initSceneTheme(pane);
        return new Scene(pane, 256, 256);                                                           // setting the size of the scene as 256x256 pixels
    }

}
