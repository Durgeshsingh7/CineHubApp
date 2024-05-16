package cinehubapp;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;

public class ViewMoviesWindow extends Stage {

    private static final String BACKGROUND_COLOR = "#2c3e50";
    private static final String TEXT_COLOR = "white";
    private static final String CSS_STYLE = "-fx-font-size: 16; -fx-text-fill: " + TEXT_COLOR + ";";
    private static final String IMAGE_PATH_PREFIX = "/cinehubapp/Photos/";

    public ViewMoviesWindow(Stage primaryStage) {
        
        setTitle("Available Movies");
        setFullScreen(true);
        
        // application icon
        getIcons().add(new Image(getClass().getResourceAsStream(IMAGE_PATH_PREFIX + "appicon.png")));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(20);
        gridPane.setHgap(20);
        gridPane.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        Label moviesLabel = new Label("Available Movies:");
        moviesLabel.setStyle("-fx-font-size: 20; -fx-text-fill: " + TEXT_COLOR + ";");
        GridPane.setConstraints(moviesLabel, 0, 0);

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            close();
            primaryStage.setFullScreen(true); // Set primary stage full-screen again
        });

        GridPane.setConstraints(backButton, 0, 0); // Position at the top-left corner
        gridPane.getChildren().add(backButton);
    
        GridPane.setMargin(backButton, new Insets(20, 20, 0, 20));
   
        List<MovieInfo> movieInfoList = createMovieInfoList();

        for (int i = 0; i < movieInfoList.size(); i++) {
            MovieInfo movieInfo = movieInfoList.get(i);

            Image movieImage = new Image(getClass().getResourceAsStream(IMAGE_PATH_PREFIX + movieInfo.getImagePath()));
            ImageView imageView = new ImageView(movieImage);
            imageView.setFitWidth(400);
            imageView.setFitHeight(400);
            GridPane.setConstraints(imageView, 0, i + 1);

            Label movieInfoLabel = new Label("Movie Name: " + movieInfo.getName() + "\n" +
                    "Rating: " + movieInfo.getRating() + "\n" +
                    "Type: " + movieInfo.getType() + "\n" +
                    "Availability: " + movieInfo.getAvailability() + "\n" +
                    "Release Date: " + movieInfo.getReleaseDate());
            movieInfoLabel.setStyle(CSS_STYLE);
            GridPane.setConstraints(movieInfoLabel, 1, i + 1);

            gridPane.getChildren().addAll(imageView, movieInfoLabel);
        }

        scrollPane.setContent(gridPane);

        Scene scene = new Scene(scrollPane, Color.web("#34495e"));
        setScene(scene);
    }

    private List<MovieInfo> createMovieInfoList() {
        List<MovieInfo> movieInfoList = new ArrayList<>();

        movieInfoList.add(new MovieInfo("Kung Fu Panda 4", "9.0", "Action, Thriller", "2D, IMAX, 4DX", "March 03, 2024", "kung.jpg"));
        movieInfoList.add(new MovieInfo("Deadpool 3", "9.5", "Action,Drama,Comedy", "2D, IMAX, 4DX", "March 26, 2024", "dead.jpg"));
        movieInfoList.add(new MovieInfo("All India Rank", "8.2", "Comedy, Drama", "2D, IMAX, 4DX", "March 15, 2024", "allindiarank.jpg"));
        movieInfoList.add(new MovieInfo("Fighter", "8.9", "Action, Drama", "2D, IMAX, 4DX", "April 04, 2024", "fighter.jpg"));
        movieInfoList.add(new MovieInfo("Godzilla x Kong : New Emipire", "9.2", "Action, Adventure, Fantacy", "2D, IMAX, 4DX", "April 20, 2024", "godzilla.jpg"));
        movieInfoList.add(new MovieInfo("Hanuman", "6.8", "Thriller, Drama", "2D, IMAX, 4DX", "March 21, 2024", "hanuman.jpg"));
        movieInfoList.add(new MovieInfo("Planet of Kindom Of The Apes", "8.9", "Thriller, Adventure", "2D, IMAX, 4DX", "March 26, 2024", "kingdom.jpg"));
        movieInfoList.add(new MovieInfo("Shaitaan", "8.2", "Drama, Horror", "2D, IMAX, 4DX", "March 09, 2024", "shaitaan.jpg"));
        movieInfoList.add(new MovieInfo("Teri Baaton mein aisa Uljha Jiya", "9.0", "Drama, Family", "2D, IMAX, 4DX", "March 20, 2024", "teri.jpg"));
 
        return movieInfoList;
    }

    private static class MovieInfo {
        private final String name;
        private final String rating;
        private final String type;
        private final String availability;
        private final String releaseDate;
        private final String imagePath;

        public MovieInfo(String name, String rating, String type, String availability, String releaseDate, String imagePath) {
            this.name = name;
            this.rating = rating;
            this.type = type;
            this.availability = availability;
            this.releaseDate = releaseDate;
            this.imagePath = imagePath;
        }

        public String getName() {
            return name;
        }

        public String getRating() {
            return rating;
        }

        public String getType() {
            return type;
        }

        public String getAvailability() {
            return availability;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public String getImagePath() {
            return imagePath;
        }
    }

}
