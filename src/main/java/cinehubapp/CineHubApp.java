package cinehubapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CineHubApp extends Application {

    public final User authenticatedUser;
    private Stage primaryStage;

    // Constructor to receive the authenticated user
    public CineHubApp(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("CineHub Express");
        this.primaryStage.getIcons().add(new Image(getClass().getResource("/cinehubapp/Photos/appicon.png").toExternalForm()));
        this.primaryStage.setScene(createMainScene());
        this.primaryStage.setFullScreen(true);
        this.primaryStage.show();
    }

    private Scene createMainScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("root");
        borderPane.setPadding(new Insets(20, 20, 20, 20));

        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(20);

        Label introLabel = new Label("""
                Welcome to CineHub Express!
                Your premier destination for a seamless and thrilling movie ticket booking experience! 
                Immerse yourself in the world of entertainment as you navigate our user-friendly 
                interface designed for cinephiles like you. 
                Our application offers a curated selection of the latest blockbuster movies, ensuring you 
                stay up-to-date with the hottest releases.

                Embark on a journey of convenience and choice as you effortlessly explore our extensive 
                movie catalog.
                CineHub Express not only provides a visually appealing and interactive main screen but 
                also introduces you to an immersive cinema experience.
                With just a click, discover the available movies, complete with real-time seat availability 
                and enticing visuals to enhance your decision-making.

                Feel the excitement as you seamlessly transition to our booking screen, where you can 
                secure your tickets with ease.
                Whether you're a solo moviegoer or planning a night out with friends, CineHub Express 
                ensures a hassle-free booking process, letting you focus on what truly matters – 
                the magic of cinema.

                CineHub Express prides itself on a vibrant and dynamic atmosphere, highlighted by a modern and 
                sleek design.The application's full-screen display captivates users,while the carefully chosen 
                color palette creates an inviting and engaging environment.From the moment you launch the app, 
                you're greeted with a warm welcome and an overview of our offerings, setting the stage for 
                an exceptional cinematic adventure.
                """);
        introLabel.setStyle("-fx-font-size: 16; -fx-text-fill: black;");

        Button viewMoviesButton = createStyledButton("View Available Movies");
        viewMoviesButton.setOnAction(e -> showAvailableMovies());

        Button bookTicketsButton = createStyledButton("Book Tickets");
        bookTicketsButton.setOnAction(e -> showBookingScreen());

        Button exitButton = createStyledButton("Exit");
        exitButton.setOnAction(e -> {
            System.out.println("Exiting the application. Goodbye!");
            Platform.exit();
        });

        grid.add(introLabel, 0, 0, 1, 2);
        GridPane.setMargin(introLabel, new Insets(0, 0, 20, 0));  // Add bottom margin to introLabel
        grid.add(viewMoviesButton, 0, 2);
        grid.add(bookTicketsButton, 0, 3);
        grid.add(exitButton, 0, 4);
        
        VideoDisplayPane videoDisplayPane = new VideoDisplayPane(authenticatedUser);
        borderPane.setRight(videoDisplayPane);

        borderPane.setCenter(grid);

        return new Scene(borderPane, Color.web("#34495e"));

    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("button");
        return button;
    }

    private void showAvailableMovies() {
        Media media = new Media(getClass().getResource("/cinehubapp/LoadingScreen.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        Stage videoStage = new Stage();
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(1370);  // Set the width of the media view
        mediaView.setFitHeight(768); // Set the height of the media view

        BorderPane root = new BorderPane(mediaView);
        Scene scene = new Scene(root);
        videoStage.setScene(scene);
        videoStage.setFullScreen(true);
        videoStage.show();

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            videoStage.close(); // Close the video stage
            openAvailableMovies(); // Open the available movies window
        });

        mediaPlayer.play(); // Start playing the media
    }

    private void showBookingScreen() {
        Media media = new Media(getClass().getResource("/cinehubapp/LoadingScreen.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        Stage videoStage = new Stage();
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(1370);  // Set the width of the media view
        mediaView.setFitHeight(768); // Set the height of the media view

        BorderPane root = new BorderPane(mediaView);
        Scene scene = new Scene(root);
        videoStage.setScene(scene);
        videoStage.setFullScreen(true);
        videoStage.show();

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            videoStage.close(); // Close the video stage
            openBookingScreen(); // Open the Booking window
        });

        mediaPlayer.play(); // Start playing the media
    }

    private void openAvailableMovies() {
        ViewMoviesWindow viewMoviesWindow = new ViewMoviesWindow(primaryStage);
        viewMoviesWindow.show();
    }

    private void openBookingScreen() {
        BookingScreen bookingScreen = new BookingScreen(authenticatedUser); 
        primaryStage.setFullScreen(true);
        bookingScreen.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
