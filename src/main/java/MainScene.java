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

        Text t = new Text();                                                                        // creating text box
        t.setFont(new Font(18));                                                                    // setting font size
        t.setText("Welcome to Boggle!");                                                            // giving user a welcome message

        Button play = new Button("Play!"), settings = new Button("Settings");                       // creating 2 buttons
        play.setOnMouseClicked((e) -> {                                                             // checking mouse button click
            SetupScene.currentPlayer = 0;                                                           // starts the setup with the first player
            BoggleGUI.stage.setScene(SetupScene.getScene());                                        // change scene if Play! button is pressed
        });
        settings.setOnMouseClicked(e -> BoggleGUI.stage.setScene(SettingsScene.getScene()));        // change scene if Settings button is pressed

        VBox pane = new VBox(t, new Text(), play, new Text(), settings);                            // creating verticle GUI layout for text, play button, and settings button
        pane.setAlignment(Pos.CENTER);                                                              // aligning GUI to center
        pane.getStylesheets().add(WinScene.class.getResource("jmetro.css").toExternalForm());
        return new Scene(pane, 256, 256);                                                           // setting the size of the scene as 256x256 pixels
    }

}
