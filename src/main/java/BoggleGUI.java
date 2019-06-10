import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BoggleGUI  extends Application {

    public static Stage stage;

    public static void initSceneTheme(Pane s) {
        s.getStylesheets().add("https://fonts.googleapis.com/css?family=Nunito:200,300,400,600,700,800,900");
        s.getStylesheets().add(BoggleGUI.class.getResource("jmetro.css").toExternalForm());
    }

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
