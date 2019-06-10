import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class GameScene {

    private static GridPane gridPane = new GridPane();

    public static Scene getScene() {
        BoggleGUI.stage.setTitle("Boggle");

        gridPane = new GridPane();
        for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(50));
            gridPane.getRowConstraints().add(new RowConstraints(50));
        }

        gridPane.add(new Text(Boggle.getCurrentPlayer().getName() + ": " + Boggle.getCurrentPlayer().getScore()), 0, 0);

        Text countdown = new Text("" + Boggle.maxTimePerTurn);
        gridPane.add(countdown, Boggle.BOARD_SIZE - 1, 0);

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
                    if (!currentPlayerName.equals(Boggle.getCurrentPlayer().getName())) {
                        return;
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }

                countdown.setText("" + i);
            }
            Platform.runLater(() -> {
                if (!Boggle.players.isEmpty())
                    Boggle.nextTurn();
            });
        }).start();

        gridPane.setAlignment(Pos.CENTER);
        updateBoard();

        TextField word = new TextField();
        Button submit = new Button("Submit");
        gridPane.add(new Text("Word:"), 0, Boggle.BOARD_SIZE + 1);
        gridPane.add(word, 1, Boggle.BOARD_SIZE + 1);
        gridPane.add(submit, Boggle.BOARD_SIZE - 1, Boggle.BOARD_SIZE + 1);

        submit.setOnMouseClicked(e -> Boggle.handleTurn(word.getCharacters().toString()));

        gridPane.getStylesheets().add(WinScene.class.getResource("jmetro.css").toExternalForm());

        return new Scene(gridPane, 600, 600);
    }

    private static void updateBoard() {
        for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
            for (int j = 0; j < Boggle.BOARD_SIZE; j++) {
                gridPane.add(new Text("" + Boggle.board[i][j]), i, j + 1);
            }
        }
    }

}
