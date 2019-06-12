// creates main scene for boggle GUI
// using javaFX
import javafx.geometry.Pos;                                                                         // importing required libraries
import javafx.scene.Scene;                                                                          // importing required libraries
import javafx.scene.control.Button;                                                                 // importing required libraries
import javafx.scene.layout.VBox;                                                                    // importing required libraries
import javafx.scene.text.Font;                                                                      // importing required libraries
import javafx.scene.text.Text;                                                                      // importing required libraries

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
 * Class: MainScene
 * Description: Deals with the logic behind displaying the ingame GUI, timers, buttons, rendering board, etc.
 */

public class MainScene {

    // returns an instance of the front page scene (screen)
    public static Scene getScene() {
        BoggleGUI.stage.setTitle("Boggle Game"); // naming the window Boggle Game

        VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);

        Text t = new Text("Welcome to Boggle!"); // creating title box

        Button play = new Button("Play!"), settings = new Button("Settings"); // creating 2 buttons
        play.setOnAction(e -> { // Start setup process when play button is pressed
            BoggleGUI.playSound("bell.wav");
            SetupScene.currentPlayer = 0; // reset player to 0
            BoggleGUI.stage.setScene(SetupScene.getScene());
        });
        play.setDefaultButton(true); // when user presses enter press this button
        settings.setOnAction(e -> BoggleGUI.stage.setScene(SettingsScene.getScene())); // change to settings scene if settings button is pressed

        // add components to layout
        pane.getChildren().addAll(t, new Text(), play, new Text(), settings);

        BoggleGUI.initSceneTheme(pane);
        return new Scene(pane, 256, 256); // setting the size of the scene as 256x256 pixels
    }

}
