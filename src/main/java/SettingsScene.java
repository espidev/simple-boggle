// Assignment: Boggle Program
// Name: Devin, Felix, Raz
// Date: June 12th, 2019

// importing required libraries

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class SettingsScene {

    private static Spinner<Integer> buildSpinner(int min, int max, int initVal) {
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initVal));
        return spinner;
    }

    public static Scene getScene() {
        // setting title and layout of scene
        BoggleGUI.stage.setTitle("Settings");
        GridPane pane = new GridPane();

        // adding padding for visual pleasure
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setVgap(5);
        pane.setHgap(5);

        // creating text fields
        Spinner<Integer> pointsToPlay = buildSpinner(1, 10000, Boggle.pointsToPlay),
            numberOfPlayers = buildSpinner(1, 20, Boggle.numberOfPlayers),
            minimumWordLength = buildSpinner(1, 20, Boggle.minimumWordLength),
            maxTimePerTurn = buildSpinner(1, 10000, Boggle.maxTimePerTurn),
            roundsUntilAllowShakeBoard = buildSpinner(1, 10000, Boggle.roundsUntilAllowShakeBoard);

        CheckBox highlightAsYouType = new CheckBox(),
                allowShakeBoard = new CheckBox();
        highlightAsYouType.setSelected(Boggle.highlightAsYouType);
        allowShakeBoard.setSelected(Boggle.allowShakeBoard);

        // creating the panes and setting the position of the panes
        pane.add(new Text("Points to play:"), 0, 0);
        pane.add(pointsToPlay, 1, 0);
        pane.add(new Text("Number of players:"), 0, 2);
        pane.add(numberOfPlayers, 1, 2);
        pane.add(new Text("Minimum word length:"), 0, 4);
        pane.add(minimumWordLength, 1, 4);
        pane.add(new Text("Max seconds per turn:"), 0, 6);
        pane.add(maxTimePerTurn, 1, 6);
        pane.add(new Text("Rounds to wait to allow board shake:"), 0, 8);
        pane.add(roundsUntilAllowShakeBoard, 1, 8);
        pane.add(new Text("Highlight as you type:"), 0, 10);
        pane.add(highlightAsYouType, 1, 10);
        pane.add(new Text("Allow shaking of board:"), 0, 12);
        pane.add(allowShakeBoard, 1, 12);

        // creating save button
        Button save = new Button("Save");

        // setting button click action and default state
        save.setOnAction(e -> {
            Boggle.minimumWordLength = minimumWordLength.getValue();
            Boggle.numberOfPlayers = numberOfPlayers.getValue();
            Boggle.pointsToPlay = pointsToPlay.getValue();
            Boggle.maxTimePerTurn = maxTimePerTurn.getValue();
            Boggle.roundsUntilAllowShakeBoard = roundsUntilAllowShakeBoard.getValue();
            Boggle.highlightAsYouType = highlightAsYouType.isSelected();
            Boggle.allowShakeBoard = allowShakeBoard.isSelected();
            BoggleGUI.stage.setScene(MainScene.getScene());
        });
        save.setDefaultButton(true);

        // creating save button pane and setting position of the pane\
        pane.add(save, 1, 14);

        BoggleGUI.initSceneTheme(pane);
        return new Scene(pane, 500, 400);
    }
}
