import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

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
 * Class: SetupScene
 * Description: Scene that represents the setup process (adding players and their names)
 */

public class SetupScene {

    // current player we are setting up
    static int currentPlayer = 0;

    // return the current setup scene we need
    public static Scene getScene() {
        BoggleGUI.stage.setTitle("Setup player " + (currentPlayer+1));

        // create layout pane
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setVgap(5);
        pane.setHgap(5);

        // add name input box
        TextField name = new TextField();
        pane.add(new Text("Type Player " + (currentPlayer+1) + "'s Name:"), 0, 0);
        pane.add(name, 0, 1);

        // add save button
        Button save = new Button("Save");
        save.setOnAction(e -> { // when clicked listener
            // add player to game
            Boggle.players.add(new Player(name.getCharacters().toString()));
            if (currentPlayer == Boggle.numberOfPlayers-1) {
                Boggle.startGame(); // start game if all the players have been setup
            } else { // otherwise, go to the next player to setup
                currentPlayer++;
                BoggleGUI.stage.setScene(SetupScene.getScene());
            }
        });
        save.setDefaultButton(true); // make save button default (press enter to click)
        pane.add(save, 1, 1);

        pane.setAlignment(Pos.CENTER);

        BoggleGUI.initSceneTheme(pane); // initialize our theme palette
        return new Scene(pane, 350, 256);
    }
}
