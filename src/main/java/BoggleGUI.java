import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

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
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();

        AudioClip sound = new AudioClip(BoggleGUI.class.getResource("indoors.wav").toExternalForm());
        sound.play();
    }

    public static void main(String[] args) {
        launch();
    }
}
