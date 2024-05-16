package cinehubapp;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ErrorWindow extends Stage {

    public ErrorWindow(String errorMessage, Stage primaryStage) {
        setTitle("Error");
        getIcons().add(new Image(getClass().getResourceAsStream("/cinehubapp/Photos/" + "appicon.png")));
        setResizable(false);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(15);
        gridPane.setHgap(10);
        gridPane.setBackground(new Background(new BackgroundFill(Color.web("#ecf0f1"), CornerRadii.EMPTY, Insets.EMPTY)));

        Label errorLabel = new Label(errorMessage);
        errorLabel.setTextFill(Color.BLACK);
        GridPane.setConstraints(errorLabel, 0, 0);

        Button tryAgainButton = new Button("Try Again");
        tryAgainButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        tryAgainButton.setOnAction(e -> {
            close();
            openLoginWindow(primaryStage); // Show the login window again
        });
        GridPane.setConstraints(tryAgainButton, 0, 1);

        gridPane.getChildren().addAll(errorLabel, tryAgainButton);

        Scene scene = new Scene(gridPane, 300,90);
        scene.setFill(Color.TRANSPARENT); // Make scene background transparent
        setScene(scene);
    }

    private void openLoginWindow(Stage primaryStage) {
        LoginWindow loginWindow = new LoginWindow(primaryStage, (username, password) -> {
            // Authenticate the user when login is successful
            User authenticatedUser = authenticateUser(username, password);

            if (authenticatedUser != null) {
                int userId = authenticatedUser.getId(); 
                // Open the main application window after successful login
                openCineHubSplashScreen(primaryStage, authenticatedUser);
            } else {
               showErrorWindow(primaryStage);
            }
        });

        loginWindow.show();
    }

    private void openCineHubSplashScreen(Stage primaryStage, User authenticatedUser) {
        // Launch CineHubSplashScreen
        CineHubSplashScreen cineHubSplashScreen = new CineHubSplashScreen(authenticatedUser);
        cineHubSplashScreen.start(new Stage()); 
        
        // Close the login window
        primaryStage.close();
    }
    
    private User authenticateUser(String username, String password) {
        // Return the authenticated user or null if authentication fails
        return DatabaseConnector.authenticateUser(username, password);
    }
    
    private void showErrorWindow(Stage primaryStage) {
        ErrorWindow errorWindow = new ErrorWindow("Invalid username or password. Please try again.", primaryStage);
        errorWindow.show();
    }
}
