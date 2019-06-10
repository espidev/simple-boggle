import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class SetupScene {

    static int currentPlayer = 0;

    public static Scene getScene() {
        BoggleGUI.stage.setTitle("Setup player " + (currentPlayer+1));

        GridPane pane = new GridPane();

        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setVgap(5);
        pane.setHgap(5);

        TextField name = new TextField();
        pane.add(new Text("Name:"), 0, 0);
        pane.add(name, 0, 1);

        Button save = new Button("Save");
        save.setOnAction(e -> {
            Boggle.players.add(new Player(name.getCharacters().toString()));
            if (currentPlayer == Boggle.numberOfPlayers-1) {
                Boggle.startGame();
            } else {
                currentPlayer++;
                BoggleGUI.stage.setScene(SetupScene.getScene());
            }
        });
        save.setDefaultButton(true);
        pane.add(save, 1, 1);

        pane.setAlignment(Pos.CENTER);

        pane.getStylesheets().add(WinScene.class.getResource("jmetro.css").toExternalForm());

        return new Scene(pane, 350, 256);
    }
}
