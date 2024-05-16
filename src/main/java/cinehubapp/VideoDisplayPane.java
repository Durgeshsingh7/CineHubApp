package cinehubapp;

import java.time.format.DateTimeFormatter;
import javafx.animation.FadeTransition;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VideoDisplayPane extends StackPane {

    private int userId;
    private final User authenticatedUser;

    private static final String[] VIDEO_FILENAMES = {
        "/cinehubapp/Videos/All India Rank.mp4",
        "/cinehubapp/Videos/Deadpool & Wolverine.mp4",
        "/cinehubapp/Videos/Fighter.mp4",
        "/cinehubapp/Videos/Godzilla x Kong.mp4",
        "/cinehubapp/Videos/Hanuman.mp4",
        "/cinehubapp/Videos/Kingdom of the Planet of the Apes.mp4",
        "/cinehubapp/Videos/Kung Fu Panda 4.mp4",
        "/cinehubapp/Videos/Shaitaan.mp4",
        "/cinehubapp/Videos/Teri Baaton Mein Aisa Uljha Jiya.mp4"
    };

    private static final String SPLASH_TEXT_COLOR = "White";

    private MediaPlayer mediaPlayer;

    public VideoDisplayPane(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        initVideoDisplay();
    }

    private void initVideoDisplay() {
        String randomVideoPath = getRandomVideo(Arrays.asList(VIDEO_FILENAMES));
        Media media = new Media(getClass().getResource(randomVideoPath).toExternalForm());
        mediaPlayer = new MediaPlayer(media);

        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(600);
        mediaView.setFitHeight(400);

        Text splashText = new Text("Now Showing");
        splashText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        splashText.setFill(Color.web(SPLASH_TEXT_COLOR));
        splashText.setTextAlignment(TextAlignment.CENTER);
        splashText.setOpacity(0.0);

        getChildren().addAll(mediaView, splashText);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), splashText);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });

        mediaPlayer.play();

        // Add a profile icon to the top right corner
        Image profileImage = new Image(getClass().getResource("/cinehubapp/profileicon.png").toExternalForm());
        ImageView profileIcon = new ImageView(profileImage);
        profileIcon.setFitWidth(50);
        profileIcon.setFitHeight(50);
        userId = authenticatedUser.getId();
        profileIcon.setOnMouseClicked(e -> openUserProfileWindow(userId));

        // Add a wallet icon below the profile icon
        Image walletImage = new Image(getClass().getResource("/cinehubapp/Photos/Wallet.png").toExternalForm());
        ImageView walletIcon = new ImageView(walletImage);
        walletIcon.setFitWidth(40);
        walletIcon.setFitHeight(40);

        // Fetch the wallet amount for the current user from the database
        double walletAmount = DatabaseConnector.getWalletAmountForCurrentUser(userId);

        // Create a Text node to display the wallet amount
        Text walletText = new Text(String.format("₹%.2f", walletAmount));
        walletText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        walletText.setFill(Color.BLACK);
        walletText.setTextAlignment(TextAlignment.CENTER);

        // Create an HBox to hold the wallet icon and wallet amount text
        HBox walletBox = new HBox(5, walletIcon, walletText);
        walletBox.setAlignment(Pos.TOP_RIGHT);
        walletBox.setPadding(new Insets(10));

        // Create an HBox to hold the profile icon and wallet display
        HBox profileBox = new HBox(10, profileIcon, walletBox);
        profileBox.setAlignment(Pos.TOP_RIGHT);
        profileBox.setPadding(new Insets(10));

        // Add the profile box to the stack pane
        getChildren().addAll(profileBox);
    }

    private String getRandomVideo(List<String> videoFiles) {
        Random random = new Random();
        return videoFiles.get(random.nextInt(videoFiles.size()));
    }

    private void openUserProfileWindow(int userId) {
        // Fetch user information from the database based on userId
        UserInfo userInfo = DatabaseConnector.getUserInfo(userId);

        // Check if user information is retrieved successfully
        if (userInfo != null) {
            // Create a new UserProfileWindow instance with the retrieved user information
            new UserProfileWindow(userInfo, this::updateUserInfoInDatabase).show();
        } else {
            // Handle the case where user information is not found
            showErrorWindow("User Info Not Found", "User info not found for user ID: " + userId);
            System.out.println("User info not found for user ID: " + userId);
        }
    }

    private void updateUserInfoInDatabase(UserInfo userInfo) {
        // Call the DatabaseConnector method to update the user information
        DatabaseConnector.updateUserInfo(userInfo);
    }

    private void showErrorWindow(String title, String message) {
        Stage errorStage = new Stage();
        errorStage.initModality(Modality.APPLICATION_MODAL);
        errorStage.setTitle(title);

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Label errorMessageLabel = new Label(message);

        Button editProfileButton = new Button("Edit Profile");
        editProfileButton.setOnAction(e -> {
            // Close the error window if it's open
            if (errorStage.isShowing()) {
                errorStage.close();
            }
            openEditProfileWindow(userId);
        });
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> errorStage.close());

        layout.getChildren().addAll(errorMessageLabel, editProfileButton, closeButton);

        Scene scene = new Scene(layout, 300, 100);
        errorStage.setScene(scene);
        errorStage.show();
    }

    private void openEditProfileWindow(int userID) {
        Stage editProfileStage = new Stage();
        editProfileStage.initModality(Modality.APPLICATION_MODAL);
        editProfileStage.setTitle("Edit Profile");

        // Create form components
        Label fullNameLabel = new Label("Full Name:");
        TextField fullNameField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label dobLabel = new Label("Date of Birth:");
        DatePicker dobPicker = new DatePicker();

        Label addressLabel = new Label("Address:");
        TextField addressField = new TextField();

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            // Save the edited profile information to the database
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String dob = dobPicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String address = addressField.getText();

            // Update the user profile in the database
            boolean success = DatabaseConnector.updateUserProfile(userID, fullName, email, dob, address);
            if (success) {
                editProfileStage.close();
                openUserProfileWindow(userID);
            } else {
                System.out.println("Error Inserting Data of UserId: " + userId);
            }
        });

        // Layout the form components
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                fullNameLabel, fullNameField,
                emailLabel, emailField,
                dobLabel, dobPicker,
                addressLabel, addressField,
                saveButton
        );

        Scene scene = new Scene(layout, 330, 300);
        editProfileStage.setScene(scene);
        editProfileStage.show();
    }
}
