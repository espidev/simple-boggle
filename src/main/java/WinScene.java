import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Collections;

public class WinScene {
    public static Scene getScene(Player p) {
        BoggleGUI.stage.setTitle("Boggle: " + p.getName() + " won!");
        GridPane pane = new GridPane();

        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setVgap(30);
        pane.setHgap(30);
        pane.setAlignment(Pos.CENTER);

        Boggle.players.sort(Collections.reverseOrder());

        int i = 0;
        for (; i < Boggle.players.size(); i++) {
            pane.add(new Text("" + (i+1)), 0, i*2);
            pane.add(new Text(Boggle.players.get(i).getName() + " (" + Boggle.players.get(i).getScore() + ")"), 1, i*2);
        }

        Button ok = new Button("Okay");

        ok.setOnAction(e -> BoggleGUI.stage.setScene(MainScene.getScene()));
        ok.setDefaultButton(true);

        pane.add(ok, 1, i*2);

        pane.getStylesheets().add(WinScene.class.getResource("jmetro.css").toExternalForm());

        return new Scene(pane, 256, 256);
    }
}
