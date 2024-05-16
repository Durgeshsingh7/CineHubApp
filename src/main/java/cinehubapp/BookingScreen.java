package cinehubapp;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class BookingScreen extends Stage {

    private int userId;
    private final double normalPrice = 200; // Price for Normal seat
    private final double premiumPrice = 250; // Price for Premium seat
    private final double royalPrice = 300; // Price for Royal seat
    private int numberOfSeats = 1; // Default number of seats
    private Label amountValueLabel;
    public ComboBox<String> timeComboBox;
    private final Label selectedSeatsLabel = new Label("Selected Seats:");
    private final List<String> selectedSeatsList;
    
    private boolean movieSelected = false;
    private boolean seatsSelected = false;
    private boolean paymentInfoEntered = false;
    private final Label errorMessageLabel;
    private ComboBox<String> paymentComboBox;
    public ComboBox<String> movieComboBox;
    public DatePicker datePicker;
    private Button bookButton; // Added to access it globally

    private final User authenticatedUser;
    
    public BookingScreen(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser; // Set the authenticatedUser field
        selectedSeatsList = new ArrayList<>();
        setTitle("Booking Screen");
        setResizable(false);
        getIcons().add(new Image(getClass().getResourceAsStream("/cinehubapp/Photos/" + "appicon.png")));

        // Initialize UI components
        GridPane gridPane = createGridPane();

        // Add components to the scene
        Scene scene = new Scene(gridPane, Color.web("#34495e"));
        setScene(scene);

        errorMessageLabel = new Label();
        errorMessageLabel.setTextFill(Color.RED);
        GridPane.setConstraints(errorMessageLabel, 1, 8); // Position the label at the bottom
        gridPane.getChildren().add(errorMessageLabel);
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // 1. Movie Selection
        Label movieLabel = new Label("1. Select Movie:* ");
        GridPane.setConstraints(movieLabel, 0, 0);
        movieComboBox = createMovieComboBox();
        GridPane.setConstraints(movieComboBox, 1, 0);
        movieComboBox.setOnAction(e -> movieSelected = !movieComboBox.getValue().equals("Select Movie"));
        
        // 2. Date Selection
        Label dateLabel = new Label("2. Select Date:* ");
        GridPane.setConstraints(dateLabel, 0, 1);
        datePicker = new DatePicker(); // Create a DatePicker control
        GridPane.setConstraints(datePicker, 1, 1);

        // 3. Time Selection
        Label timeLabel = new Label("3. Select Time:* ");
        GridPane.setConstraints(timeLabel, 0, 2);
        timeComboBox = new ComboBox<>();
        timeComboBox.getItems().addAll("9:00 AM","10:00 AM", "12:00 PM","2:00 PM", "4:00 PM", "6:00 PM","8:00 PM"); // Add time options
        GridPane.setConstraints(timeComboBox, 1, 2);
        timeComboBox.setOnAction(e -> seatsSelected = timeComboBox.getValue() != null);

        // 4. Number of Seats
        Label seatLabel = new Label("4. Select Number of Seats:* ");
        GridPane.setConstraints(seatLabel, 0, 3);
        Label numberOfSeatsLabel = new Label("Number of Seats: " + numberOfSeats);
        GridPane.setConstraints(numberOfSeatsLabel, 1, 3);
        Button increaseSeatsButton = new Button("+");
        increaseSeatsButton.setOnAction(e -> increaseNumberOfSeats(numberOfSeatsLabel));
        GridPane.setConstraints(increaseSeatsButton, 2, 3);
        Button decreaseSeatsButton = new Button("-");
        decreaseSeatsButton.setOnAction(e -> decreaseNumberOfSeats(numberOfSeatsLabel));
        GridPane.setConstraints(decreaseSeatsButton, 3, 3);

        // 5. Seat Selection
        Label seatSelectionLabel = new Label("5. Select Seats:* ");
        GridPane.setConstraints(seatSelectionLabel, 0, 4);
        Button seatSelectionButton = new Button("Select Seats");
        GridPane.setConstraints(seatSelectionButton, 1, 4);
        seatSelectionButton.setOnAction(e -> showSeatSelectionPopup());

        // selectedSeatsLabel just below the seat selection option
        selectedSeatsLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 14));
        GridPane.setConstraints(selectedSeatsLabel, 0, 5, 2, 1); // Span 2 columns for full width
        selectedSeatsLabel.setStyle("-fx-text-fill: Black;"); // Optionally set text color

        // 6. Payment Mode
        Label paymentLabel = new Label("6. Select Payment Mode:* ");
        GridPane.setConstraints(paymentLabel, 0, 6);

        paymentComboBox = new ComboBox<>();
        paymentComboBox.getItems().addAll("Wallet", "UPI", "Netbanking");
        paymentComboBox.setPromptText("Select Payment Mode");
        GridPane.setConstraints(paymentComboBox, 1, 6);
        paymentComboBox.setOnAction(e -> paymentInfoEntered = paymentComboBox.getValue() != null);

        // 7. Display Total Amount
        Label totalAmountLabel = new Label("Total Amount:");
        totalAmountLabel.setFont(Font.font("Calibri", 16)); // Set font size and style
        amountValueLabel = new Label("Rs. " + calculateTotalAmount(selectedSeatsList));
        amountValueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18)); // Set font size and style
        GridPane.setConstraints(totalAmountLabel, 0, 7);
        GridPane.setConstraints(amountValueLabel, 1, 7);

        bookButton = new Button("Book");
        bookButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 14px;");
        bookButton.setOnAction(e -> bookTickets());
        GridPane.setConstraints(bookButton, 0, 8);

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px;");
        closeButton.setOnAction(e -> close());
        GridPane.setConstraints(closeButton, 0, 9);

        // Add components to grid pane
        gridPane.getChildren().addAll(
                movieLabel, movieComboBox, dateLabel, datePicker,
                timeLabel, timeComboBox, seatLabel, numberOfSeatsLabel,
                increaseSeatsButton, decreaseSeatsButton, seatSelectionLabel,
                seatSelectionButton, paymentLabel, paymentComboBox,
                totalAmountLabel, amountValueLabel, bookButton, closeButton,
                selectedSeatsLabel // Add the selected seats label here
        );

        return gridPane;
    }

    private ComboBox<String> createMovieComboBox() {
        List<String> movies = Arrays.asList("Kung Fu Panda 4", "Deadpool 3", "All India Rank", "Fighter", "Godzilla x Kong : New Emipire", "Hanuman", "Planet of Kindom Of The Apes", "Shaitaan", "Teri Baaton mein aisa Uljha Jiya"); // Sample movie list
        ObservableList<String> movieList = FXCollections.observableArrayList(movies);
        ComboBox<String> comboBox = new ComboBox<>(movieList);
        comboBox.setValue("Select Movie");
        return comboBox;
    }

    private void increaseNumberOfSeats(Label numberOfSeatsLabel) {
        numberOfSeats++;
        numberOfSeatsLabel.setText("Number of Seats: " + numberOfSeats);
        amountValueLabel.setText("Rs. " + calculateTotalAmount(selectedSeatsList)); // Update total amount
    }
    
    private void decreaseNumberOfSeats(Label numberOfSeatsLabel) {
        if (numberOfSeats > 1) {
            numberOfSeats--;
            numberOfSeatsLabel.setText("Number of Seats: " + numberOfSeats);
            amountValueLabel.setText("Rs. " + calculateTotalAmount(selectedSeatsList)); // Update total amount
        }
    }

    private void showSeatSelectionPopup() {
        SeatSelectionPopup seatSelectionPopup = new SeatSelectionPopup(this, numberOfSeats); // Pass reference to this BookingScreen
        seatSelectionPopup.show();
    }

    private double calculateTotalAmount(List<String> selectedSeatsList) {
        double totalAmount = 0;

        // Iterate through selected seats and calculate total amount
        for (String seat : selectedSeatsList) {
            // Check the type of seat and add corresponding price to the total amount
            char seatType = seat.charAt(0); // Assuming the seat type is represented by the first character of the seat string
            switch (seatType) {
                case 'A', 'B', 'C' -> totalAmount += normalPrice;
                case 'D', 'E' -> totalAmount += premiumPrice;
                case 'F' -> totalAmount += royalPrice;
            }
        }

        return totalAmount;
    }

    public void updateSelectedSeatsLabel(List<String> selectedSeatsList) {
        this.selectedSeatsList.clear(); // Clear existing contents
        this.selectedSeatsList.addAll(selectedSeatsList); // Add all elements from the parameter list
        selectedSeatsLabel.setText("Selected Seats: " + String.join(", ", this.selectedSeatsList));
        amountValueLabel.setText("Rs. " + calculateTotalAmount(this.selectedSeatsList)); // Update total amount based on selected seats
    }
    
    private boolean areFieldsFilled() {
        // Check if all required fields are filled
        return movieSelected && seatsSelected && paymentInfoEntered;
    }
    
    private void bookTickets() {
        if (areFieldsFilled()) {
            String selectedPaymentMode = paymentComboBox.getValue();
            if (selectedPaymentMode.equals("Wallet")) {
                // Get the authenticated user's ID
                userId = authenticatedUser.getId();
                 
                // Get booking information
                String movieName = movieComboBox.getValue();
                String bookingDate = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String bookingTime = timeComboBox.getValue();
                String seats = String.join(", ", selectedSeatsList);
                double totalAmount = calculateTotalAmount(selectedSeatsList);
                double bookingAmount = calculateTotalAmount(selectedSeatsList);
                
                boolean BookingSuccessful = DatabaseConnector.saveBooking(userId, movieName, bookingDate, bookingTime, seats, totalAmount, selectedPaymentMode);
                // Perform database update to deduct the booking amount from the wallet
                boolean bookingSuccessful = DatabaseConnector.deductAmountFromWallet(userId, bookingAmount);

                if (bookingSuccessful && BookingSuccessful) {
                    playVideo();
                    System.out.println("Booking successful!");
                } else {
                    errorMessageLabel.setText("Insufficient balance in wallet.");
                }
            } else {
                // Display feature coming soon message
                 FeatureComingSoonWindow featureComingSoonWindow = new FeatureComingSoonWindow("Feature Coming Soon...");
                 featureComingSoonWindow.show();
            }
        } else {
            errorMessageLabel.setText("Please fill in all the required fields.");
        }
    }
    
    private void resetForm() {
        // Reset all fields to their initial state
        movieSelected = false;
        seatsSelected = false;
        paymentInfoEntered = false;
        amountValueLabel.setText("Rs. 0.00");
        selectedSeatsList.clear();
        // Reset ComboBoxes
        timeComboBox.getSelectionModel().clearSelection();
        // Reset other UI components as needed
    }

    private void playVideo() {
        // Load the video from resources
        Media media = new Media(getClass().getResource("/cinehubapp/Payment.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        // Create a media view to display the video
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(800); // Adjust width as needed
        mediaView.setFitHeight(600); // Adjust height as needed

        // Create a scene to hold the media view
        Scene videoScene = new Scene(new Group(mediaView), 800, 600); // Adjust dimensions as needed

        // Create a stage to display the video
        Stage videoStage = new Stage();
        videoStage.setScene(videoScene);
        videoStage.setTitle("Payment");
        getIcons().add(new Image(getClass().getResourceAsStream("/cinehubapp/Photos/" + "appicon.png")));

        // Show the video stage
        videoStage.show();

        // Play the video
        mediaPlayer.play();
        

        // Close the video stage automatically when the video ends
        mediaPlayer.setOnEndOfMedia(() -> {
        videoStage.close();
        displayTicket(); // Display ticket after successful booking
        resetForm(); // Reset the form after successful booking
        });
    }

    private void displayTicket() {
        // Create a GridPane to organize ticket information
        GridPane ticketGrid = new GridPane();
        ticketGrid.setPadding(new Insets(20));
        ticketGrid.setHgap(10);
        ticketGrid.setVgap(10);
        
        // Add expressions of gratitude
        Label gratitudeLabel = new Label("""
                                         Thank you for choosing CineHub Express,
                                         we deeply appreciate your decision to join us for 
                                         an unforgettable cinematic experience. 
                                         Your choice means a lot to us, and we're thrilled 
                                         to have you as part of our valued audience. 
                                         As we embark on this cinematic journey together, 
                                         we aim to provide you with not just a movie but an 
                                         immersive experience filled with excitement,
                                         laughter, and memories that will last a lifetime.
                                        """);
        gratitudeLabel.setFont(Font.font("Sabon Next LT", FontWeight.NORMAL, 14));
        GridPane.setConstraints(gratitudeLabel, 0, 0, 2, 1);
        ticketGrid.getChildren().add(gratitudeLabel);

        // Add ticket details
        Label movieLabel = new Label("Movie:");
        Label dateLabel = new Label("Date:");
        Label timeLabel = new Label("Time:");
        Label seatsLabel = new Label("Seats:");

        // Add movie name, date, time, and selected seats dynamically
        Label movieValueLabel = new Label(movieComboBox.getValue());
        Label dateValueLabel = new Label(datePicker.getValue().toString());
        Label timeValueLabel = new Label(timeComboBox.getValue());
        Label seatsValueLabel = new Label(String.join(", ", selectedSeatsList));

        // Apply styles to labels
        applyTicketLabelStyle(movieLabel);
        applyTicketLabelStyle(dateLabel);
        applyTicketLabelStyle(timeLabel);
        applyTicketLabelStyle(seatsLabel);

        // Add labels to the grid
        ticketGrid.addRow(1, movieLabel, movieValueLabel);
        ticketGrid.addRow(2, dateLabel, dateValueLabel);
        ticketGrid.addRow(3, timeLabel, timeValueLabel);
        ticketGrid.addRow(4, seatsLabel, seatsValueLabel);

        // Add notes on precautions, rules, and regulations
        Label notesLabel = new Label("""
                                     Please note: Follow all safety precautions and 
                                     abide by the rules and regulations while 
                                     enjoying the movie.
                                     """);
        notesLabel.setFont(Font.font("Sabon Next LT", FontWeight.NORMAL, 14));
        notesLabel.setTextFill(Color.RED);
        GridPane.setConstraints(notesLabel, 0, 5, 2, 1);
        ticketGrid.getChildren().add(notesLabel);

        // Create a scene for the ticket
        Scene ticketScene = new Scene(ticketGrid, 400, 400); // Adjust dimensions as needed

        // Create the ticket window
        Stage ticketStage = new Stage();
        ticketStage.setScene(ticketScene);
        ticketStage.setTitle("Movie Ticket");
        getIcons().add(new Image(getClass().getResourceAsStream("/cinehubapp/Photos/" + "appicon.png")));

        // Show the ticket window
        ticketStage.show();
    }

    private void applyTicketLabelStyle(Label label) {
        label.setFont(Font.font("Arial", 14));
        label.setStyle("-fx-font-weight: bold;");
    }

}
