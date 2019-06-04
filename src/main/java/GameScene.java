import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class GameScene {

    public static GridPane gridPane = new GridPane();

    public static Scene getScene() {
        BoggleGUI.stage.setTitle("Boggle");

        gridPane = new GridPane();
        for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(30));
            gridPane.getRowConstraints().add(new RowConstraints(30));
        }
        //gridPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.setAlignment(Pos.CENTER);
        updateBoard();
        return new Scene(gridPane, 500, 500);
    }

    public static void updateBoard() {
        for (int i = 0; i < Boggle.BOARD_SIZE; i++) {
            for (int j = 0; j < Boggle.BOARD_SIZE; j++) {
                gridPane.add(new Text(""+Boggle.board[i][j]), i, j);
            }
        }
    }

}
