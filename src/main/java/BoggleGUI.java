import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
   Assignment: Boggle Program

   Copyright 2019 Devin, Raz, Felix

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

/*
 * Class Name: BoggleGUI
 * Description: Entry point for JFX GUI, setting stage, utilities for GUI related tasks
 */

public class BoggleGUI  extends Application {

    public static Stage stage;

    // takes in a javafx node and themes it, and adds our font
    public static void initSceneTheme(Pane s) {
        s.getStylesheets().add("https://fonts.googleapis.com/css?family=Nunito:200,300,400,600,700,800,900");
        s.getStylesheets().add(BoggleGUI.class.getResource("theme.css").toExternalForm());
    }

    // helper method to play a sound
    public static void playSound(String name) {
        AudioClip sound = new AudioClip(BoggleGUI.class.getResource(name).toExternalForm());
        sound.play();
    }

    @Override
    public void start(Stage stage) {
        BoggleGUI.stage = stage;
        stage.setTitle("Boggle Game");
        stage.setScene(MainScene.getScene()); // get front page scene
        stage.setOnCloseRequest(e -> { // close process when x button is pressed
            Platform.exit();
            System.exit(0); // exit process
        });
        stage.show(); // show the main GUI

        // play game song
        MediaPlayer s = new MediaPlayer(new Media(BoggleGUI.class.getResource("indoors.wav").toExternalForm()));
        s.setOnEndOfMedia(() -> s.seek(Duration.ZERO)); // repeat at end of song
        s.play();
    }

    public static void main(String[] args) {
        launch();
    }
}
