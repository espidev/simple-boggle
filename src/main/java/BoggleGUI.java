import javafx.application.Application;
import javafx.stage.Stage;

public class BoggleGUI  extends Application {

    public static Stage stage;

    @Override
    public void start(Stage stage) {
        BoggleGUI.stage = stage;
        stage.setTitle("Boggle Game");
        /*String javaVersion = Systems.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();*/

        stage.setScene(MainScene.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
