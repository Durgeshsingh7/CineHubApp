package cinehubapp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.function.Consumer;
import javafx.scene.control.Alert.AlertType;

public class UserProfileWindow extends Stage {
    private final UserInfo userInfo;
    private final Consumer<UserInfo> onSave;

    private TextField fullNameField;
    private TextField emailField;
    private DatePicker dateOfBirthPicker;
    private TextField addressField;
    private Button saveButton;
    private Button closeButton;
    private Button leftArrowButton;
    private Button rightArrowButton;
    private Button okButton;
    private Button editButton;
    private Label fullNameLabel;
    private Label emailLabel;
    private Label dateOfBirthLabel;
    private Label addressLabel;
    private StackPane profileImagePane;
    public static int selectedProfileIndex;

    public UserProfileWindow(UserInfo userInfo, Consumer<UserInfo> onSave) {
        this.userInfo = userInfo;
        this.onSave = onSave;

        setTitle("User Profile");
        setResizable(false);
        getIcons().add(new Image(getClass().getResourceAsStream("/cinehubapp/Photos/appicon.png")));

        initializeUI(); // Initialize UI elements
        populateFields(); // Populate fields with user information

        GridPane gridPane = createGridPane();
        profileImagePane = createProfileImagePane();
        gridPane.add(profileImagePane, 0, 0, 2, 1); // Add profile image at top

        Scene scene = new Scene(gridPane, 320, 470); // Adjusted scene size
        setScene(scene);
    }

    private StackPane createProfileImagePane() {
        Circle clip = new Circle(100); // Create a circular clip
        clip.centerXProperty().bind(clip.radiusProperty()); // Center clip horizontally
        clip.centerYProperty().bind(clip.radiusProperty()); // Center clip vertically

        ImageView profileImageView = new ImageView(new Image(getClass().getResourceAsStream("/cinehubapp/pp" + DatabaseConnector.getProfileImageIndex(userInfo.getUserId()) + ".png"))); // Set default profile image
        profileImageView.setFitWidth(200);
        profileImageView.setFitHeight(200);
        profileImageView.setClip(clip); // Apply circular clip
        profileImageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.7), 10, 0, 0, 0);");

        StackPane.setAlignment(profileImageView, Pos.CENTER);
        profileImageView.setTranslateX(40);

        profileImagePane = new StackPane();
        profileImagePane.setId("profileImagePane");
        profileImagePane.setMaxWidth(200);
        profileImagePane.setMaxHeight(200);
        profileImagePane.getChildren().add(profileImageView); // Add the profileImageView to the StackPane

        return profileImagePane;
    }

    private void initializeUI() {
        // Initialize UI elements
        fullNameField = new TextField();
        emailField = new TextField(); 
        dateOfBirthPicker = new DatePicker();
        addressField = new TextField();
        saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        saveButton.setOnAction(e -> saveProfile());
        closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        closeButton.setOnAction(e -> close());
        editButton = new Button("Edit Profile Image");
        editButton.setOnAction(e -> handleEditButtonClick());
    }

    private void populateFields() {
        if (userInfo != null) {
            fullNameField.setText(userInfo.getFullName());
            emailField.setText(userInfo.getEmail());

            Date dateOfBirth = userInfo.getDateOfBirth();
            if (dateOfBirth != null) {
                dateOfBirthPicker.setValue(dateOfBirth.toLocalDate());
            }

            addressField.setText(userInfo.getAddress());
        }
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(15);
        gridPane.setHgap(10);
        gridPane.setStyle("-fx-background-color: white;");

        fullNameLabel = new Label("Full Name:");
        emailLabel = new Label("Email:");
        dateOfBirthLabel = new Label("Date of Birth:");
        addressLabel = new Label("Address:");

        GridPane.setConstraints(editButton, 1, 1);
        GridPane.setConstraints(fullNameLabel, 0, 2);
        GridPane.setConstraints(fullNameField, 1, 2);
        GridPane.setConstraints(emailLabel, 0, 3);
        GridPane.setConstraints(emailField, 1, 3);
        GridPane.setConstraints(dateOfBirthLabel, 0, 4);
        GridPane.setConstraints(dateOfBirthPicker, 1, 4);
        GridPane.setConstraints(addressLabel, 0, 5);
        GridPane.setConstraints(addressField, 1, 5);
        GridPane.setConstraints(saveButton, 0, 6);
        GridPane.setConstraints(closeButton, 1, 6);

        gridPane.getChildren().addAll(
                fullNameLabel, fullNameField, emailLabel, emailField,
                dateOfBirthLabel, dateOfBirthPicker, addressLabel, addressField,
                saveButton, closeButton,editButton
        );

        return gridPane;
    }

    private void saveProfile() {
        userInfo.setFullName(fullNameField.getText());
        userInfo.setEmail(emailField.getText());

        LocalDate dateOfBirth = dateOfBirthPicker.getValue();
        userInfo.setDateOfBirth(dateOfBirth != null ? Date.valueOf(dateOfBirth) : null);

        userInfo.setAddress(addressField.getText());

        onSave.accept(userInfo);
        close();
    }

    private void handleEditButtonClick() {
        // Get the parent GridPane
        GridPane parentGridPane = (GridPane) editButton.getParent();

        // Remove the edit button
        parentGridPane.getChildren().remove(editButton);
        parentGridPane.getChildren().remove(fullNameField);
        parentGridPane.getChildren().remove(emailField);
        parentGridPane.getChildren().remove(dateOfBirthPicker);
        parentGridPane.getChildren().remove(addressField);
        
        //Remove the labels and fields associated with user information
        parentGridPane.getChildren().removeAll(fullNameLabel, emailLabel, dateOfBirthLabel, addressLabel,saveButton,closeButton);
                                            
        // Create left and right arrow buttons
        leftArrowButton = new Button("<");
        rightArrowButton = new Button(">");
        okButton = new Button("OK");

        // Set translations to position buttons centrally below the profile image
        leftArrowButton.setTranslateX(15);
        rightArrowButton.setTranslateX(215);
        okButton.setTranslateX(115);

        // Set event handlers
        leftArrowButton.setOnAction(e -> handleLeftArrowButtonClick());
        rightArrowButton.setOnAction(e -> handleRightArrowButtonClick());
        okButton.setOnAction(e -> handleOkButtonClick());

        // Add buttons to the GridPane
        parentGridPane.add(leftArrowButton, 1, 1);
        parentGridPane.add(rightArrowButton, 1, 1);
        parentGridPane.add(okButton, 1, 1);
    }

    private void handleOkButtonClick() {
        // Save the selected profile image index
        selectedProfileIndex = userInfo.getSelectedProfileIndex();
        userInfo.setSelectedProfileIndex(selectedProfileIndex);
        userInfo.setProfileImageIndex(selectedProfileIndex);
        boolean result = DatabaseConnector.updateProfileImageIndex(userInfo.getUserId(), selectedProfileIndex);
 
        // Update the profile image index in the database
        if (result) {
            // Show a notification window
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("User profile image changed successfully!");
            alert.showAndWait();

            // Close the current window
            ((Stage) okButton.getScene().getWindow()).close();
            // Open a new UserProfileWindow
            UserProfileWindow newWindow = new UserProfileWindow(userInfo, onSave);
            newWindow.show();
        } else {
            // Show an error notification if the update fails
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to update user profile image!");
            alert.showAndWait();
        }
    }

    private void handleLeftArrowButtonClick() {
        // Decrement profile image index
    userInfo.decrementProfileImageIndex();
    
    // Update profile image
    updateProfileImage();
    }

    private void handleRightArrowButtonClick() {
         // Increment profile image index
    userInfo.incrementProfileImageIndex();
    
    // Update profile image
    updateProfileImage();
    }
    
    private void updateProfileImage() {
        // Get the profile image
        profileImagePane = (StackPane) getScene().lookup("#profileImagePane");
        ImageView profileImageView = (ImageView) profileImagePane.getChildren().get(0);
        // Update profile image
        profileImageView.setImage(new Image(getClass().getResourceAsStream("/cinehubapp/pp" + userInfo.getProfileImageIndex() + ".png")));
    }
}