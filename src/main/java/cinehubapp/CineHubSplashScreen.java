package cinehubapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CineHubSplashScreen extends Application {

    private static final String SPLASH_BACKGROUND_COLOR_START = "#232323";
    private static final String SPLASH_BACKGROUND_COLOR_END = "#141414";

    private final User authenticatedUser;

    public CineHubSplashScreen(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CineHub Express");
        StackPane root = new StackPane();

        // Apply a fading background gradient
        root.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, " +
                SPLASH_BACKGROUND_COLOR_START + " 0%, " + SPLASH_BACKGROUND_COLOR_END + " 100%);");

        primaryStage.getIcons().add(new Image(getClass().getResource("/cinehubapp/Photos/appicon.png").toExternalForm()));

        // Load video from resources
        Media media = new Media(getClass().getResource("/cinehubapp/StartingWindow.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        MediaView mediaView = new MediaView(mediaPlayer);
        root.getChildren().add(mediaView);

        Scene scene = new Scene(root, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Play the video and close the splash screen when finished
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            primaryStage.close();
            Platform.runLater(() -> openMainApplicationWindow());
        });

        mediaPlayer.play();
    }

    private void openMainApplicationWindow() {
        // Launch your main application window (replace with your main class)
        new CineHubApp(authenticatedUser).start(new Stage());
    }
}
