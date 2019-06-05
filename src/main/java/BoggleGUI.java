import javafx.application.Application;
import javafx.stage.Stage;

public class BoggleGUI  extends Application {

    public static Stage stage;

    @Override
    public void start(Stage stage) {
        BoggleGUI.stage = stage;
        stage.setTitle("Boggle Game");
        stage.setScene(MainScene.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
