package cinehubapp;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Open login window initially
        openLoginWindow(primaryStage);
    }

    private void openLoginWindow(Stage primaryStage) {
        LoginWindow loginWindow = new LoginWindow(primaryStage, (username, password) -> {
            // Authenticate the user when login is successful
            User authenticatedUser = authenticateUser(username, password);

            if (authenticatedUser != null) {
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
