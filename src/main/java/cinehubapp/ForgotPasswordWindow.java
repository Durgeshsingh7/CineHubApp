package cinehubapp;

import javax.mail.*;
import java.util.Properties;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForgotPasswordWindow extends Stage {

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordWindow.class);

    public ForgotPasswordWindow() {
        setTitle("Forgot Password");
        getIcons().add(new Image(getClass().getResourceAsStream("/cinehubapp/Photos/" + "appicon.png")));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(15);
        gridPane.setHgap(10);
        gridPane.setStyle("-fx-background-color: #ecf0f1;");

        Label emailLabel = new Label("Enter your Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        GridPane.setConstraints(emailLabel, 0, 0);
        GridPane.setConstraints(emailField, 1, 0);

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        submitButton.setOnAction(e -> submitEmail(emailField.getText()));
        GridPane.setConstraints(submitButton, 1, 1);

        gridPane.getChildren().addAll(emailLabel, emailField, submitButton);

        Scene scene = new Scene(gridPane, 300, 150, Color.web("#bdc3c7"));
        setScene(scene);
    }

    private void submitEmail(String email) {
        if (isValidEmail(email)) {
            String recoveryPassword = generateRandomPassword();

            // Implement your logic to send an email with recoveryPassword to the user
            sendRecoveryEmail(email, recoveryPassword);

            showAlert("Email Sent", "Recovery email has been sent to your email address.");
            close();
        } else {
            showAlert("Invalid Email", "Please enter a valid email address.");
        }
    }

    private boolean isValidEmail(String email) {
        // Implement your email validation logic (e.g., using regex)
        // For simplicity, this example assumes any non-empty string is valid.
        return !email.trim().isEmpty();
    }

    private String generateRandomPassword() {
        // For simplicity, this example generates a random 6-character password.
        return "Recovery" + (int) (Math.random() * 1000000);
    }

    private void sendRecoveryEmail(String email, String recoveryPassword) {
        // Replace these values with your actual email server details
        String host = "smtp.gmail.com";
        String port = "587";
        String username = "cinehubexpressapp@gmail.com";
        String password = "LetmeWashYourFace:..:2";

        // Set JavaMail properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Password Recovery");
            message.setText("Your recovery password is: " + recoveryPassword);

            // Send the message
            Transport.send(message);

            logger.info("Recovery email sent successfully!");

        } catch (MessagingException e) {
            logger.error("Error sending recovery email", e);
            showAlert("Error", "Failed to send recovery email. Please try again.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
