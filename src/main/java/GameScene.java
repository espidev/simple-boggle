import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameScene {

    private static GridPane charGrid = new GridPane(); // grid pane, storing the GUI for the board
    private static Text[][] charRepGrid = new Text[Boggle.BOARD_SIZE][Boggle.BOARD_SIZE]; // grid of text objects (each character)

    private static void startTimer(Text countdown) {
        // countdown
        new Thread(() -> {
            String currentPlayerName = Boggle.getCurrentPlayer().getName();
            for (int i = Boggle.maxTimePerTurn; i >= 0; i--) {
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
                countdown.setText("Time Left: " + i);
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

            if (!flagged[nx][ny] && (Boggle.board[nx][ny] == str.charAt(searchIndex)) && recursiveFlagWord(nx, ny, searchIndex + 1, str, flagged)) {
                flagged[x][y] = true;
                found = true;
            }
        }
        return found;
    }

    private static void highlightOnType(String word) {
        boolean[][] flagged = new boolean[Boggle.BOARD_SIZE][Boggle.BOARD_SIZE];
        if (word.length() != 0) {
            word = word.toUpperCase();
            for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
                for (int j = 0; j < Boggle.BOARD_SIZE; j++) {
                    if (Boggle.board[i][j] == word.charAt(0)) {
                        recursiveFlagWord(i, j, 1, word, flagged);
                    }
                }
            }
        }

        for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
            for (int j = 0; j < Boggle.BOARD_SIZE; j++) {
                if (flagged[i][j]) {
                    charRepGrid[i][j].setStyle("-fx-effect: dropshadow(three-pass-box, #e91e63, 10, 0, 0, 0);");
                    charRepGrid[i][j].setFill(Color.web("#e91e63"));
                } else {
                    charRepGrid[i][j].setStyle("-fx-effect: none;");
                    charRepGrid[i][j].setFill(Color.BLACK);
                }
            }
        }
    }

    public static Scene getScene() {
        BoggleGUI.stage.setTitle("Boggle");

        // container
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
        startTimer(countdown);
        right.getChildren().add(countdown);

        // show player list
        Text title = new Text("Players");
        title.setFont(Font.font("Nunito", FontWeight.BLACK, 18));
        title.setFill(Color.WHITE);
        right.getChildren().add(title);

        for (Player p : Boggle.players) {
            Text t = new Text(p.getName() + ": " + p.getScore());
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
        word.setOnKeyTyped(e -> highlightOnType(word.getText()));

        Button submit = new Button("Submit");
        // when player enters word
        submit.setOnAction(e -> Boggle.handleTurn(word.getCharacters().toString()));
        submit.setDefaultButton(true);
        bottom.getChildren().addAll(word, submit);

        BoggleGUI.initSceneTheme(container);
        return new Scene(container, 500, 500);
    }

    private static void renderBoard() {
        for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
            for (int j = 0; j < Boggle.BOARD_SIZE; j++) {
                charRepGrid[i][j] = new Text("" + Boggle.board[i][j]);
                charGrid.add(charRepGrid[i][j], i, j);
            }
        }
    }

}
