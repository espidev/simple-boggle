import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameScene {

    private static GridPane charGrid = new GridPane();

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

                countdown.setText("" + i);
            }
            Platform.runLater(() -> {
                if (!Boggle.players.isEmpty())
                    Boggle.nextTurn();
            });
        }).start();
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
        updateBoard();

        // center the grid
        container.setCenter(charGrid);



        // right side elements
        VBox right = new VBox();
        container.setRight(right);
        right.setPadding(new Insets(10));
        right.setSpacing(8);
        right.setStyle("-fx-text-fill: white; -fx-background-color: #e91e63; -fx-effect: dropshadow(three-pass-box, #e91e63, 10, 0, 0, 0);");

        Text countdown = new Text("" + Boggle.maxTimePerTurn);
        startTimer(countdown);
        countdown.setStyle("-fx-text-fill: white;");
        right.getChildren().add(countdown);

        Text title = new Text("Players");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        right.getChildren().add(title);

        for (Player p : Boggle.players) {
            right.getChildren().add(new Text(p.getName() + ": " + p.getScore()));
        }


        // bottom elements
        TextField word = new TextField();
        Button submit = new Button("Submit");
        submit.setOnMouseClicked(e -> Boggle.handleTurn(word.getCharacters().toString()));
        HBox bottom = new HBox(new Text("Word:"), word, submit);
        container.setBottom(bottom);
        bottom.setPadding(new Insets(15, 12, 15, 12));
        bottom.setSpacing(10);
        bottom.setStyle("-fx-background-color: #f8bbd0;");

        container.getStylesheets().add(WinScene.class.getResource("jmetro.css").toExternalForm());

        return new Scene(container, 500, 500);
    }

    private static void updateBoard() {
        for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
            for (int j = 0; j < Boggle.BOARD_SIZE; j++) {
                charGrid.add(new Text("" + Boggle.board[i][j]), i, j);
            }
        }
    }

}
