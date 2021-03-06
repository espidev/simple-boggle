import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
 * Class: GameScene
 * Description: Deals with the logic behind displaying the ingame GUI, timers, buttons, rendering board, etc.
 */

public class GameScene {

    private static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 500;
    public static boolean firstRun = false;

    public static int countdownSeconds; // value on the timer clock
    public static int currentRound = 1; // current round we are on
    public static int numberOfPasses = 0; // number of consecutive passes that have happened
    public static boolean gameFreeze = false; // whether or not input should be taken (word)

    private static GridPane charGrid = new GridPane(); // grid pane, storing the GUI for the board
    private static Text[][] charRepGrid = new Text[Boggle.BOARD_SIZE][Boggle.BOARD_SIZE]; // grid of text objects (each character)

    // modal objects
    private static BorderPane modal = new BorderPane();
    private static Text modalText = new Text();

    private static StackPane stackContainer = new StackPane(); // root game container

    // helper method to show the message on a pink rectangle in front of the game
    public static void showModal(String message, int seconds, Runnable runAfter) {
        gameFreeze = true;
        modalText.setText(message); // make sure text is set with parameter

        // fade in the modal
        FadeTransition ft = new FadeTransition(Duration.millis(500), modal);
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.play(); // start transistion
        stackContainer.getChildren().add(modal); // add the modal after to the scene

        new Thread(() -> { // remove modal later without blocking jfx thread
            Boggle.threadSleep(1000 * seconds); // wait before removing

            // fade out transition
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.play();
            Boggle.threadSleep(500);

            // remove the modal from the scene
            Platform.runLater(() -> stackContainer.getChildren().remove(modal));
            gameFreeze = false;
            runAfter.run();
        }).start();
    }

    // helper method to start the countdown timer (on top right of GUI)
    private static void startTimer(Text countdown) {
        // countdown on separate thread
        new Thread(() -> {

            String currentPlayerName = Boggle.getCurrentPlayer().getName();

            // loop seconds down
            for (GameScene.countdownSeconds = Boggle.maxTimePerTurn; countdownSeconds >= 0; countdownSeconds--) {
                Boggle.threadSleep(1000); // wait for a second

                try { // check if the current pass has ended already
                    if (!currentPlayerName.equals(Boggle.getCurrentPlayer().getName())) {
                        return;
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
                // change text
                countdown.setText("Time Left: " + countdownSeconds);
            }
            // countdown has ended
            Platform.runLater(() -> {
                if (Boggle.players.size() == 1) { // if single player mode, exit game
                    Boggle.winGame(Boggle.getCurrentPlayer());
                    return;
                }

                if (!Boggle.players.isEmpty()) { // go to the next turn if the game hasn't ended
                    numberOfPasses++; // counts as pass
                    Boggle.nextTurn();
                }
            });
        }).start();
    }

    // helper recursive method to find and flag characters of word that player is currently typing
    private static boolean recursiveFlagWord(int x, int y, int searchIndex, String str, boolean[][] flagged, boolean[][] visited) {
        // if the current processed word length is already the same length of word to search
        if (searchIndex == str.length()) {
            flagged[x][y] = true; // flag last character
            return true; // return to prevent going out of bounds
        }

        visited[x][y] = true;

        // directions to move over (x, y)
        int[][] boardDirection = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
        boolean found = false;

        // loop over possible directions on the board
        for (int[] direct : boardDirection) {
            int nx = x + direct[0], ny = y + direct[1];
            if (nx >= Boggle.BOARD_SIZE || nx < 0 || ny >= Boggle.BOARD_SIZE || ny < 0) continue; // check out of bounds

            // if the character at this new spot is the same character as the one in the word we are searching for
            // if further recursive calls find the complete word
            // and has not been visited
            if (!visited[nx][ny] && (Boggle.board[nx][ny] == str.charAt(searchIndex)) && recursiveFlagWord(nx, ny, searchIndex + 1, str, flagged, visited)) {
                flagged[x][y] = true; // flag this spot pink
                found = true;
            }
        }
        // backtrack out (save memory)
        visited[x][y] = false;
        return found;
    }

    // method called when a user types a letter
    // takes in the full text as parameter
    private static void highlightOnType(String word) {
        boolean[][] flagged = new boolean[Boggle.BOARD_SIZE][Boggle.BOARD_SIZE],
                visited = new boolean[Boggle.BOARD_SIZE][Boggle.BOARD_SIZE];

        // if the typed word is longer than 0 characters (prevent out of bounds)
        if (word.length() != 0) {
            word = word.toUpperCase(); // ignore case
            // loop over each character
            for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
                for (int j = 0; j < Boggle.BOARD_SIZE; j++) {
                    if (Boggle.board[i][j] == word.charAt(0)) { // check if the first character is the word's first character
                        // recursively flag characters
                        recursiveFlagWord(i, j, 1, word, flagged, visited);
                    }
                }
            }
        }

        // set effects for flagged characters
        for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
            for (int j = 0; j < Boggle.BOARD_SIZE; j++) {
                if (flagged[i][j]) { // if it was flagged, add shadow
                    charRepGrid[i][j].setText("" + Boggle.board[i][j]);
                    charRepGrid[i][j].setStyle("-fx-effect: dropshadow(three-pass-box, #e91e63, 10, 0, 0, 0);");
                    charRepGrid[i][j].setFill(Color.web("#e91e63"));
                } else { // otherwise, set to black
                    charRepGrid[i][j].setText("" + Boggle.board[i][j]);
                    charRepGrid[i][j].setStyle("-fx-effect: none;");
                    charRepGrid[i][j].setFill(Color.BLACK);
                }
            }
        }
    }

    // returns an instance of the game screen based on the current player
    public static Scene getScene() {
        BoggleGUI.stage.setTitle("Boggle");

        // complete container for stacking layers
        stackContainer = new StackPane();

        // main game container
        BorderPane container = new BorderPane();

        // init grid of characters
        charGrid = new GridPane();
        for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
            charGrid.getColumnConstraints().add(new ColumnConstraints(50));
            charGrid.getRowConstraints().add(new RowConstraints(50));
        }
        charGrid.setAlignment(Pos.CENTER);
        renderBoard();

        // center the grid
        container.setCenter(charGrid);

        // ~~~~~~
        // right side elements
        VBox right = new VBox();
        container.setRight(right);
        right.setPadding(new Insets(15));
        right.setSpacing(8);
        right.setStyle("-fx-text-fill: white; -fx-background-color: #e91e63; -fx-effect: dropshadow(three-pass-box, #e91e63, 10, 0, 0, 0);");

        // countdown timer
        if (Boggle.players.size() != 1 || firstRun) { // reset the countdown if not singleplayer mode
            countdownSeconds = Boggle.maxTimePerTurn;
        }
        Text countdown = new Text("Time Left: " + countdownSeconds);
        countdown.setFill(Color.WHITE);
        right.getChildren().add(countdown); // add countdown clock to GUI

        // show player list
        Text pTitle = new Text("Players");
        pTitle.setFont(Font.font("Nunito", FontWeight.BLACK, 22));
        pTitle.setFill(Color.WHITE);
        right.getChildren().add(pTitle);

        // loop over to players and add to player list
        for (Player p : Boggle.players) {
            Text t;
            if (p.getName().equals(Boggle.getCurrentPlayer().getName())) { // if it's this player's turn
                t = new Text(p.getName() + ": " + p.getScore() + " ←");
                t.setFont(Font.font("Nunito", FontWeight.BOLD, 22));
            } else { // normal player
                t = new Text(p.getName() + ": " + p.getScore());
            }
            t.setFill(Color.WHITE);
            right.getChildren().add(t);
        }

        // ~~~~~~
        BorderPane leftContainer = new BorderPane();
        leftContainer.setPadding(new Insets(15));
        leftContainer.setStyle("-fx-text-fill: #e91e63; -fx-background-color: white; -fx-effect: dropshadow(three-pass-box, #e91e63, 10, 0, 0, 0);");
        container.setLeft(leftContainer);

        // left elements
        VBox left = new VBox();
        leftContainer.setCenter(left);
        left.setSpacing(8);

        // used words
        Text uTitle = new Text("Used Words");
        uTitle.setFont(Font.font("Nunito", FontWeight.BLACK, 22));
        uTitle.setFill(Color.web("#e91e63"));
        left.getChildren().add(uTitle);

        // scroll pane to hold words
        ScrollPane words = new ScrollPane();
        words.setStyle("-fx-color: white; -fx-background-color: white;");
        left.getChildren().add(words);
        String scrollContent = "";
        for (String word : Boggle.getCurrentPlayer().getUsedWords()) {
            scrollContent += word + "\n";
        }
        words.setContent(new Text(scrollContent));

        // exit game button
        Button exit = new Button("Exit Game");
        exit.setAlignment(Pos.CENTER);
        exit.setOnAction(e -> {
            Boggle.players.clear();
            BoggleGUI.stage.setScene(MainScene.getScene());
        });
        leftContainer.setBottom(exit);

        // ~~~~~~
        // bottom elements
        HBox bottom = new HBox(new Text("Word:"));
        bottom.setPadding(new Insets(15, 12, 15, 12));
        bottom.setSpacing(10);
        bottom.setStyle("-fx-background-color: #f8bbd0;");
        container.setBottom(bottom);

        // input field for word
        TextField word = new TextField();
        Platform.runLater(word::requestFocus); // highlight
        // highlight letters that player is typing
        if (Boggle.highlightAsYouType) {
            word.setOnKeyReleased(e -> highlightOnType(word.getText()));
        }

        Button submit = new Button("Enter");
        // when player enters word
        submit.setOnAction(e -> {
            if (!gameFreeze) { // if the game is not frozen at this point (modal is not shown)
                Boggle.handleTurn(word.getCharacters().toString()); // send the word to be processed for points
            }
        });
        submit.setDefaultButton(true); // pressing enter will trigger this button

        // pass button
        Button pass = new Button("Pass");
        pass.setOnAction(e -> {
           if (!gameFreeze) { // go to next turn when clicked
               numberOfPasses++; // counts as a pass
               Boggle.nextTurn();
           }
        });

        Button shakeBoard = new Button("Shake Board");
        // when player wants to shake up board
        shakeBoard.setOnAction(e -> {
            if (!gameFreeze) {
                currentRound = 1; // reset round timer
                numberOfPasses = 0;
                bottom.getChildren().remove(shakeBoard); // remove button
                Boggle.generateBoard(); // generate a new board
                highlightOnType(word.getText()); // render the new board
            }
        });

        // if the correct number of rounds passes, add shake board button
        if (Boggle.allowShakeBoard && numberOfPasses >= Boggle.players.size()*2) {
            bottom.getChildren().addAll(word, submit, pass, shakeBoard);
        } else {
            bottom.getChildren().addAll(word, submit, pass);
        }

        // ~~~~~~
        // On top layer for alerts (modal)

        modal = new BorderPane();
        Rectangle r = new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        r.setFill(Color.web("#e91e63"));
        r.setOpacity(0.8);
        modalText = new Text();
        modalText.setFill(Color.WHITE);
        modalText.setFont(new Font("Nunito", 24));
        modal.getChildren().addAll(r);
        modal.setCenter(modalText);

        // ~~~~~~

        stackContainer.getChildren().addAll(container);
        // show player's turn modal
        if (Boggle.players.size() != 1) { // don't show if singleplayer
            showModal(Boggle.getCurrentPlayer().getName() + "'s Turn", 2, () -> startTimer(countdown));
        } else if (firstRun) { // singleplayer start countdown
            startTimer(countdown);
        }

        BoggleGUI.initSceneTheme(stackContainer);
        firstRun = false;
        return new Scene(stackContainer, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    // get the current board and render it on GUI
    private static void renderBoard() {
        // loop over each board character
        for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
            for (int j = 0; j < Boggle.BOARD_SIZE; j++) {
                // set the grid spot to the specified char
                charRepGrid[i][j] = new Text("" + Boggle.board[i][j]);
                charGrid.add(charRepGrid[i][j], i, j);
            }
        }
    }

}
