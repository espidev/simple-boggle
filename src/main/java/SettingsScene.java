// Assignment: Boggle Program
// Name: Devin, Felix, Raz
// Date: June 12th, 2019

// importing required libraries
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class SettingsScene {
    public static Scene getScene() {
        // setting title and layout of scene
        BoggleGUI.stage.setTitle("Settings");
        GridPane pane = new GridPane();
        
        // adding padding for visual pleasure
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setVgap(5);
        pane.setHgap(5);

        // creating text fields
        TextField pointsToPlay = new TextField(""+Boggle.pointsToPlay),
        numberofPlayers = new TextField(""+Boggle.numberOfPlayers),
        minimumWordLength = new TextField(""+Boggle.minimumWordLength),
        maxTimePerTurn = new TextField(""+Boggle.maxTimePerTurn);
        CheckBox highlightAsYouType = new CheckBox();
        highlightAsYouType.setSelected(Boggle.highlightAsYouType);

        // creating the panes and setting the position of the panes
        pane.add(new Text("Points to play:"), 0, 0);
        pane.add(pointsToPlay, 1, 0);
        pane.add(new Text("Number of players:"), 0, 2);
        pane.add(numberofPlayers, 1, 2);
        pane.add(new Text("Minimum word length:"), 0, 4);
        pane.add(minimumWordLength, 1, 4);
        pane.add(new Text("Max seconds per turn:"), 0, 6);
        pane.add(maxTimePerTurn, 1, 6);
        pane.add(new Text("Highlight as you type:"), 0, 8);
        pane.add(highlightAsYouType, 1, 8);

        // creating save button
        Button save = new Button("Save");

        // setting buton click action and default state
        save.setOnAction(e -> {
            Boggle.minimumWordLength = Integer.parseInt(minimumWordLength.getCharacters().toString());
            Boggle.numberOfPlayers = Integer.parseInt(numberofPlayers.getCharacters().toString());
            Boggle.pointsToPlay = Integer.parseInt(pointsToPlay.getCharacters().toString());
            Boggle.maxTimePerTurn = Integer.parseInt(maxTimePerTurn.getCharacters().toString());
            Boggle.highlightAsYouType = highlightAsYouType.isSelected();
            BoggleGUI.stage.setScene(MainScene.getScene());
        });
        save.setDefaultButton(true);
        
        // creating save button pane and setting position of the pane\
        pane.add(save, 1, 10);

        BoggleGUI.initSceneTheme(pane);
        return new Scene(pane, 300, 300);
    }
}
