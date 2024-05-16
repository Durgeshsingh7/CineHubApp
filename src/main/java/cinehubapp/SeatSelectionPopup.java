package cinehubapp;

import static cinehubapp.DatabaseConnector.logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionPopup extends Stage {

    private final BookingScreen ownerScreen; // Reference to the BookingScreen
    private final Label selectedSeatsLabel;
    private final List<String> selectedSeatsList = new ArrayList<>(); // List to store selected seats
    private int numberOfSeatsLeft; // Number of seats left to select

    public SeatSelectionPopup(BookingScreen ownerScreen, int numberOfSeats) {
        this.ownerScreen = ownerScreen; // Initialize the reference to the BookingScreen
        this.numberOfSeatsLeft = numberOfSeats; // Set the initial number of seats left
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Seat Selection");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Create theater screen animation
        Rectangle screen = new Rectangle(600, 20, Color.BLACK);
        screen.setStroke(Color.WHITE);
        screen.setStrokeWidth(2);

        // Create color transition
        Timeline colorChange = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(screen.fillProperty(), Color.RED)),
                new KeyFrame(Duration.seconds(1), new KeyValue(screen.fillProperty(), Color.BLUE)),
                new KeyFrame(Duration.seconds(2), new KeyValue(screen.fillProperty(), Color.GREEN)),
                new KeyFrame(Duration.seconds(3), new KeyValue(screen.fillProperty(), Color.YELLOW)),
                new KeyFrame(Duration.seconds(4), new KeyValue(screen.fillProperty(), Color.ORANGE)),
                new KeyFrame(Duration.seconds(5), new KeyValue(screen.fillProperty(), Color.PURPLE)),
                new KeyFrame(Duration.seconds(6), new KeyValue(screen.fillProperty(), Color.CYAN)),
                new KeyFrame(Duration.seconds(7), new KeyValue(screen.fillProperty(), Color.MAGENTA))
        );
        colorChange.setCycleCount(Timeline.INDEFINITE);
        colorChange.play();

        StackPane screenPane = new StackPane(screen, new Label("SCREEN"));
        layout.getChildren().add(screenPane);

        // Create sections for each seat type
        layout.getChildren().addAll(
                createSeatSection("Normal - Rs 200", 'A', 'B', 'C'),
                createSeatSection("Premium - Rs 250", 'D', 'E'),
                createSeatSection("Royal - Rs 300", 'F')
        );

        selectedSeatsLabel = new Label("Selected Seats: (Seats left: " + numberOfSeatsLeft + ")");
        layout.getChildren().add(selectedSeatsLabel);

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> handleOKButtonClick());
        layout.getChildren().add(okButton);
        
        Button showAvailableSeatsButton = new Button("Show Available Seats");
        showAvailableSeatsButton.setOnAction(e -> showAvailableSeats());
        layout.getChildren().add(showAvailableSeatsButton);

        Scene scene = new Scene(layout);
        setScene(scene);
    }

    private GridPane createSeatSection(String sectionTitle, char... rows) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);

        // Title for the seat section
        Label title = new Label(sectionTitle);
        GridPane.setConstraints(title, 0, 0, rows.length + 1, 1);
        grid.getChildren().add(title);

        // Add seats for each row
        int rowIndex = 1;
        for (char row : rows) {
            Label rowLabel = new Label(String.valueOf(row));
            GridPane.setConstraints(rowLabel, 0, rowIndex);

            for (int seatNum = 1; seatNum <= 10; seatNum++) {
                Button seatButton = new Button(row + String.valueOf(seatNum));
                seatButton.setPrefWidth(60);
                seatButton.setOnAction(e -> handleSeatSelection(seatButton));
                GridPane.setConstraints(seatButton, seatNum, rowIndex);
                grid.getChildren().add(seatButton);
            }

            rowIndex++;
        }

        return grid;
    }

    private void handleSeatSelection(Button seatButton) {
        String seat = seatButton.getText();
        if (selectedSeatsList.contains(seat)) {
            selectedSeatsList.remove(seat); // Deselect the seat if already selected
            numberOfSeatsLeft++; // Increment the number of seats left
        } else if (numberOfSeatsLeft > 0) {
            selectedSeatsList.add(seat); // Select the seat if seats are left
            numberOfSeatsLeft--; // Decrement the number of seats left
        }
        updateSelectedSeatsLabel();
    }

    private void updateSelectedSeatsLabel() {
        selectedSeatsLabel.setText("Selected Seats: " + String.join(", ", selectedSeatsList)
                + " (Seats left: " + numberOfSeatsLeft + ")");
    }

    private void handleOKButtonClick() {
        // Pass the list of selected seats back to the BookingScreen
        ownerScreen.updateSelectedSeatsLabel(selectedSeatsList);
        close(); // Close the popup
    }
    
    private void showAvailableSeats() {
        // Get movie name, date, and time from wherever it's stored in your application
        String movieName = ownerScreen.movieComboBox.getValue();
        String date = ownerScreen.datePicker.getValue().toString();
        String time = ownerScreen.timeComboBox.getValue();

        // Call the fetchAvailableSeats method
        fetchAvailableSeats(movieName, date, time);
    }

    public void fetchAvailableSeats(String movieName, String date, String time) {
        // Interaction with the database to fetch available seats
        List<String> availableSeats = fetchAvailableSeatsFromDatabase(movieName, date, time);

        // Display the available seats
        displayAvailableSeats(availableSeats);
    }

    private List<String> fetchAvailableSeatsFromDatabase(String movieName, String date, String time) {
        List<String> availableSeats = new ArrayList<>();
        Connection connection = null;

        // SQL query to fetch available seats for the specified movie, date, and time
        String query = "SELECT seats FROM bookings WHERE movie_name = ? AND booking_date = ? AND booking_time = ?";

        try {
            connection = DatabaseConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, movieName);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, time);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                availableSeats.add(resultSet.getString("seats"));
            }
        } catch (SQLException e) {
            logger.error("Error Fetching Data", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection", e);
                }
            }
        }

        return availableSeats;
    }
    
    private void displayAvailableSeats(List<String> availableSeats) {
        // Get the root layout of the scene
        VBox layout = (VBox) getScene().getRoot();

        // Lookup the availableSeatsBox
        VBox availableSeatsBox = (VBox) layout.lookup("#availableSeatsBox");

        // Check if availableSeatsBox is null
        if (availableSeatsBox == null) {
            // Create a new VBox for available seats
            availableSeatsBox = new VBox();
            availableSeatsBox.setId("availableSeatsBox");

            // Add the availableSeatsBox to the layout
            layout.getChildren().add(availableSeatsBox);
        } else {
            // Clear the existing content of availableSeatsBox
            availableSeatsBox.getChildren().clear();
        }

        // Display available seats below the screen animation
        Label availableSeatsLabel = new Label("Available Seats: " + String.join(", ", availableSeats));
        availableSeatsBox.getChildren().add(availableSeatsLabel);
    }
}
