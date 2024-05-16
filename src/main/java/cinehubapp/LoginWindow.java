package cinehubapp;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.function.BiConsumer;
import javafx.scene.image.Image;

public class LoginWindow extends Stage {
    private final BiConsumer<String, String> onLogin;
    private final Stage primaryStage;

    public LoginWindow(Stage primaryStage, BiConsumer<String, String> onLogin) {
        super(); // Call to super must be the first statement in the constructor
        this.primaryStage = primaryStage;
        this.onLogin = onLogin;

        setTitle("Login");
        getIcons().add(new Image(getClass().getResourceAsStream("/cinehubapp/Photos/" + "appicon.png")));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(15);
        gridPane.setHgap(10);
        gridPane.setStyle("-fx-background-color: #ecf0f1;");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        GridPane.setConstraints(usernameLabel, 0, 0);
        GridPane.setConstraints(usernameField, 1, 0);

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        GridPane.setConstraints(passwordLabel, 0, 1);
        GridPane.setConstraints(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        loginButton.setOnAction(e -> onLoginButtonClick(usernameField.getText(), passwordField.getText()));
        GridPane.setConstraints(loginButton, 1, 2);

        Button forgotPasswordButton = new Button("Forgot Password?");
        forgotPasswordButton.setStyle("-fx-text-fill: #2980b9;");
        forgotPasswordButton.setOnAction(e -> showForgotPasswordDialog());
        GridPane.setConstraints(forgotPasswordButton, 1, 3);

        Label createAccountLabel = new Label("Don't have an account?");
        createAccountLabel.setStyle("-fx-text-fill: #7f8c8d;");
        GridPane.setConstraints(createAccountLabel, 0, 4);

        Button createAccountButton = new Button("Create New Account");
        createAccountButton.setStyle("-fx-text-fill: #e74c3c;");
        createAccountButton.setOnAction(e -> showCreateAccountDialog());
        GridPane.setConstraints(createAccountButton, 1, 4);

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        closeButton.setOnAction(e -> close());
        GridPane.setConstraints(closeButton, 1, 5);

        gridPane.getChildren().addAll(
                usernameLabel, usernameField, passwordLabel, passwordField,
                loginButton, forgotPasswordButton, createAccountLabel,
                createAccountButton, closeButton
        );

        Scene scene = new Scene(gridPane, 300, 260, Color.web("#bdc3c7"));
        setScene(scene);
    }

    private void onLoginButtonClick(String username, String password) {
        if (validateInputs(username, password)) {
            onLogin.accept(username, password);
            close();
        } else {
            new ErrorWindow("Invalid username or password. Please try again.", primaryStage).show();
            showAlert("Invalid Inputs", "Please enter valid username and password.");
        }
    }

    private boolean validateInputs(String username, String password) {
        return !username.trim().isEmpty() && !password.trim().isEmpty();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showForgotPasswordDialog() {
        ForgotPasswordWindow forgotPasswordWindow = new ForgotPasswordWindow();
        forgotPasswordWindow.show();
    }

    private void showCreateAccountDialog() {
        close();
        new RegistrationWindow(primaryStage, (u, p) -> {}).show();
    }
}
