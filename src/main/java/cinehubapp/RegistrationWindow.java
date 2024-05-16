package cinehubapp;

import static cinehubapp.DatabaseConnector.authenticateUser;
import static cinehubapp.DatabaseConnector.isUsernameAvailable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.function.BiConsumer;
import javafx.scene.image.Image;

public class RegistrationWindow extends Stage {
    private final BiConsumer<String, String> onRegistration;
    private static final Logger logger = LoggerFactory.getLogger(RegistrationWindow.class);
    Stage primaryStage;

    public RegistrationWindow(Stage primaryStage, BiConsumer<String, String> onRegistration) {
        this.primaryStage = primaryStage;
        this.onRegistration = onRegistration;

        setTitle("Register");
        setResizable(false);
        getIcons().add(new Image(getClass().getResourceAsStream("/cinehubapp/Photos/" + "appicon.png")));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(20);
        gridPane.setHgap(20);
        gridPane.setStyle("-fx-background-color: white;");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        GridPane.setConstraints(usernameLabel, 0, 0);
        GridPane.setConstraints(usernameField, 1, 0);

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        GridPane.setConstraints(passwordLabel, 0, 1);
        GridPane.setConstraints(passwordField, 1, 1);

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        // Handle registration logic when the button is clicked
        registerButton.setOnAction(e -> onRegisterButtonClick(usernameField.getText(), passwordField.getText()));
        GridPane.setConstraints(registerButton, 0, 2, 2, 1);

        // Label to navigate to the login window
        Label loginLabel = new Label("Already have an account? Login");
        loginLabel.setStyle("-fx-text-fill: #3498db; -fx-underline: true; -fx-cursor: hand;");
        loginLabel.setOnMouseClicked(event -> switchToLoginWindow(primaryStage));
        GridPane.setConstraints(loginLabel, 0, 3, 2, 1);

        gridPane.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, registerButton, loginLabel);

        Scene scene = new Scene(gridPane, Color.web("#34495e"));
        setScene(scene);
    }

    private void onRegisterButtonClick(String username, String password) {
    if (validateInputs(username, password)) {
        // Check if the username is available
        if (isUsernameAvailable(username)) {
            // Perform registration
            User registeredUser = DatabaseConnector.registerUser(username, password);
            if (registeredUser != null) {
                onRegistration.accept(username, password);
                showAlert("Registration Successful", "You have successfully registered!");
                // Log the registered user details
                logger.info("User registered - Username: {}", registeredUser.getUsername());
                // open login window after successfull registeration
                switchToLoginWindow(primaryStage);
            } else {
                showAlert("Registration Failed", "Failed to register the user. Please try again.");
            }
        } else {
            showAlert("Username Taken", "The username is already taken. Please choose another.");
        }
    } else {
        showAlert("Invalid Inputs", "Please enter a valid username and password.");
           }
    }

    private boolean validateInputs(String username, String password) {
        return !username.trim().isEmpty() && !password.trim().isEmpty();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void switchToLoginWindow(Stage primaryStage) {
        // Close the registration window and open the login window
        close();
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
    
    private void showErrorWindow(Stage primaryStage) {
        ErrorWindow errorWindow = new ErrorWindow("Invalid username or password. Please try again.", primaryStage);
        errorWindow.show();
    }
}
