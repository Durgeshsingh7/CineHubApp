package cinehubapp;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FeatureComingSoonWindow extends Stage {

    public FeatureComingSoonWindow(String message) {
        setTitle("Error");
        getIcons().add(new Image(getClass().getResourceAsStream("/cinehubapp/Photos/" + "appicon.png")));
        setResizable(false);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label messageLabel = new Label(message);
        messageLabel.setTextFill(Color.RED);
        GridPane.setConstraints(messageLabel, 0, 0);
        gridPane.getChildren().add(messageLabel);

        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> close()); // Close the window when OK button is clicked
        GridPane.setConstraints(closeButton, 0, 1);
        gridPane.getChildren().add(closeButton);

        Scene scene = new Scene(gridPane);
        setScene(scene);
    }
}
