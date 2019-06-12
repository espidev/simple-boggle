import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameScene {

    private static final int WINDOW_WIDTH = 500, WINDOW_HEIGHT = 500;

    public static int countdownSeconds;

    private static GridPane charGrid = new GridPane(); // grid pane, storing the GUI for the board
    private static Text[][] charRepGrid = new Text[Boggle.BOARD_SIZE][Boggle.BOARD_SIZE]; // grid of text objects (each character)

    private static BorderPane modal = new BorderPane();
    private static Text modalText = new Text();
    private static StackPane stackContainer = new StackPane();

    public static void showModal(String message, int seconds, Runnable runAfter) {
        modalText.setText(message);
        FadeTransition ft = new FadeTransition(Duration.millis(500), modal);
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.play();
        stackContainer.getChildren().add(modal);
        new Thread(() -> {
            try {
                Thread.sleep(1000 * seconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.play();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> stackContainer.getChildren().remove(modal));
            runAfter.run();
        }).start();
    }

    private static void startTimer(Text countdown) {
        // countdown
        new Thread(() -> {
            String currentPlayerName = Boggle.getCurrentPlayer().getName();
            for (GameScene.countdownSeconds = Boggle.maxTimePerTurn; countdownSeconds >= 0; countdownSeconds--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    if (!currentPlayerName.equals(Boggle.getCurrentPlayer().getName()))
                        return;
                } catch (IndexOutOfBoundsException ignored) {
                }
                countdown.setText("Time Left: " + countdownSeconds);
            }
            Platform.runLater(() -> {
                if (!Boggle.players.isEmpty())
                    Boggle.nextTurn();
            });
        }).start();
    }

    private static boolean recursiveFlagWord(int x, int y, int searchIndex, String str, boolean[][] flagged) {
        if (searchIndex == str.length()) {
            flagged[x][y] = true;
            return true;
        }

        int[][] boardDirection = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
        boolean found = false;

        // loop over possible directions on the board
        for (int[] direct : boardDirection) {
            int nx = x + direct[0], ny = y + direct[1];
            if (nx >= Boggle.BOARD_SIZE || nx < 0 || ny >= Boggle.BOARD_SIZE || ny < 0) continue; // check out of bounds

            if ((Boggle.board[nx][ny] == str.charAt(searchIndex)) && recursiveFlagWord(nx, ny, searchIndex + 1, str, flagged)) {
                flagged[x][y] = true;
                found = true;
            }
        }
        return found;
    }

    // method called when a user types a letter
    // takes in the full text as parameter
    private static void highlightOnType(String word) {
        boolean[][] flagged = new boolean[Boggle.BOARD_SIZE][Boggle.BOARD_SIZE];

        // if the typed word is longer than 0 characters (prevent out of bounds)
        if (word.length() != 0) {
            word = word.toUpperCase(); // ignore case
            // loop over each character
            for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
                for (int j = 0; j < Boggle.BOARD_SIZE; j++) {
                    if (Boggle.board[i][j] == word.charAt(0)) { // check if the first character is the word's first character
                        // recursively flag characters
                        recursiveFlagWord(i, j, 1, word, flagged);
                    }
                }
            }
        }

        // set effects for flagged characters
        for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
            for (int j = 0; j < Boggle.BOARD_SIZE; j++) {
                if (flagged[i][j]) { // if it was flagged, add shadow
                    charRepGrid[i][j].setStyle("-fx-effect: dropshadow(three-pass-box, #e91e63, 10, 0, 0, 0);");
                    charRepGrid[i][j].setFill(Color.web("#e91e63"));
                } else { // otherwise, set to black
                    charRepGrid[i][j].setStyle("-fx-effect: none;");
                    charRepGrid[i][j].setFill(Color.BLACK);
                }
            }
        }
    }

    public static Scene getScene() {
        BoggleGUI.stage.setTitle("Boggle");

        // complete container for stacking layers
        stackContainer = new StackPane();

        // main game container
        BorderPane container = new BorderPane();

        charGrid = new GridPane();
        for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
            charGrid.getColumnConstraints().add(new ColumnConstraints(50));
            charGrid.getRowConstraints().add(new RowConstraints(50));
        }

        charGrid.setAlignment(Pos.CENTER);
        renderBoard();

        // center the grid
        container.setCenter(charGrid);

        // right side elements
        VBox right = new VBox();
        container.setRight(right);
        right.setPadding(new Insets(10));
        right.setSpacing(8);
        right.setStyle("-fx-text-fill: white; -fx-background-color: #e91e63; -fx-effect: dropshadow(three-pass-box, #e91e63, 10, 0, 0, 0);");

        // countdown timer
        Text countdown = new Text("Time Left: " + Boggle.maxTimePerTurn);
        countdown.setFill(Color.WHITE);
        right.getChildren().add(countdown);

        // show player list
        Text title = new Text("Players");
        title.setFont(Font.font("Nunito", FontWeight.BLACK, 18));
        title.setFill(Color.WHITE);
        right.getChildren().add(title);

        for (Player p : Boggle.players) {
            Text t;
            if (p.getName().equals(Boggle.getCurrentPlayer().getName())) {
                t = new Text(p.getName() + ": " + p.getScore() + " ←");
            } else {
                t = new Text(p.getName() + ": " + p.getScore());
            }
            t.setFill(Color.WHITE);
            right.getChildren().add(t);
        }

        // bottom elements
        HBox bottom = new HBox(new Text("Word:"));
        bottom.setPadding(new Insets(15, 12, 15, 12));
        bottom.setSpacing(10);
        bottom.setStyle("-fx-background-color: #f8bbd0;");
        container.setBottom(bottom);

        // input field for word
        TextField word = new TextField();
        // highlight letters that player is typing
        if (Boggle.highlightAsYouType) {
            word.setOnKeyReleased(e -> highlightOnType(word.getText()));
        }

        Button submit = new Button("Submit");
        // when player enters word
        submit.setOnAction(e -> Boggle.handleTurn(word.getCharacters().toString()));
        submit.setDefaultButton(true);
        bottom.getChildren().addAll(word, submit);

        // ~~~~~~
        // On top layer for alerts and modal stuff

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
        // show player's turn
        showModal(Boggle.getCurrentPlayer().getName() + "'s Turn", 2, () -> startTimer(countdown));

        BoggleGUI.initSceneTheme(stackContainer);
        return new Scene(stackContainer, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    // get the current board and render it on GUI
    private static void renderBoard() {
        for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
            for (int j = 0; j < Boggle.BOARD_SIZE; j++) {
                charRepGrid[i][j] = new Text("" + Boggle.board[i][j]);
                charGrid.add(charRepGrid[i][j], i, j);
            }
        }
    }

}
