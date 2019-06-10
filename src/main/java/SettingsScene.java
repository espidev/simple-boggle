import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class SettingsScene {
    public static Scene getScene() {
        BoggleGUI.stage.setTitle("Settings");
        GridPane pane = new GridPane();

        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setVgap(5);
        pane.setHgap(5);

        TextField pointsToPlay = new TextField(""+Boggle.pointsToPlay),
                numberofPlayers = new TextField(""+Boggle.numberOfPlayers),
                minimumWordLength = new TextField(""+Boggle.minimumWordLength),
                maxTimePerTurn = new TextField(""+Boggle.maxTimePerTurn);

        pane.add(new Text("Points to play:"), 0, 0);
        pane.add(pointsToPlay, 1, 0);
        pane.add(new Text("Number of players:"), 0, 2);
        pane.add(numberofPlayers, 1, 2);
        pane.add(new Text("Minimum word length:"), 0, 4);
        pane.add(minimumWordLength, 1, 4);
        pane.add(new Text("Max seconds per turn:"), 0, 6);
        pane.add(maxTimePerTurn, 1, 6);

        Button save = new Button("Save");

        save.setOnAction(e -> {
            Boggle.minimumWordLength = Integer.parseInt(minimumWordLength.getCharacters().toString());
            Boggle.numberOfPlayers = Integer.parseInt(numberofPlayers.getCharacters().toString());
            Boggle.pointsToPlay = Integer.parseInt(pointsToPlay.getCharacters().toString());
            Boggle.maxTimePerTurn = Integer.parseInt(maxTimePerTurn.getCharacters().toString());
            BoggleGUI.stage.setScene(MainScene.getScene());
        });
        save.setDefaultButton(true);

        pane.add(save, 1, 8);

        BoggleGUI.initSceneTheme(pane);
        return new Scene(pane, 300, 256);
    }
}
