import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Collections;

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
 * Class: WinScene
 * Description: Scene that represents the window shown after a game is done
 */

class WinScene {
    public static Scene getScene(Player p) {
        BoggleGUI.stage.setTitle("Boggle: " + p.getName() + " won!");

        // initialize grid layout
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setVgap(30);
        pane.setHgap(30);
        pane.setAlignment(Pos.CENTER);

        // sort players by their score (custom comparator)
        Boggle.players.sort(Collections.reverseOrder());

        int i = 1;

        pane.add(new Text(Boggle.getCurrentPlayer().getScore() + " points! Good job!"), 0, 0);

        if (Boggle.players.size() == 1) { // singleplayer mode
            for (String word : Boggle.getCurrentPlayer().getUsedWords()) { // display words that player entered
                pane.add(new Text(word), 0, i*2);
                i++;
            }
        } else { // multiplayer mode
            for (; i < Boggle.players.size(); i++) { // loop over each player and add their winning to the screen
                pane.add(new Text("" + (i + 1)), 0, i * 2);
                pane.add(new Text(Boggle.players.get(i).getName() + " (" + Boggle.players.get(i).getScore() + ")"), 1, i * 2);
            }
        }

        // add okay button
        Button ok = new Button("Okay");

        ok.setOnAction(e -> BoggleGUI.stage.setScene(MainScene.getScene())); // return to main screen when clicked
        ok.setDefaultButton(true);

        pane.add(ok, 1, i * 2);

        BoggleGUI.initSceneTheme(pane);
        return new Scene(pane, 350, 300);
    }
}
