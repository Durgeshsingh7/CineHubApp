package cinehubapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DatabaseConnector {
    public static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/cinehubapp";
    private static final String USERNAME = "Durgesh";
    private static final String PASSWORD = "Makeyoulaugh*2";

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        connection.setAutoCommit(false);  // Set auto-commit to true
        
        return connection;
    }

    public static User authenticateUser(String username, String password) {
        String query = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    logger.info("User retrieved from database: {}", username);
                    User user = new User(username, password);
                    user.setId(userId); // Set the id retrieved from the database
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error("Error authenticating user", e);
        }
        return null;
    }

    public static User registerUser(String username, String password) {
         String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows > 0) {
            connection.commit();  // Commit the transaction
            logger.info("User registered successfully - Username: {}", username);
            return new User(username, password);
        }
    } catch (SQLException e) {
        logger.error("Error registering user", e);
    }
    return null;
    }

    public static boolean isUsernameAvailable(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If there is a result, it means the username is already taken
                return !resultSet.next();
            }
        } catch (SQLException e) {
            logger.error("Error checking username availability", e);
            return false; // Consider it as not available in case of an exception
        }
    }
    
    public static UserInfo getUserInfo(int userId) {
        String query = "SELECT * FROM user_profile WHERE user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            logger.info("User Information Retrived From Database");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(resultSet.getInt("id"));
                    userInfo.setUserId(userId);
                    userInfo.setFullName(resultSet.getString("full_name"));
                    userInfo.setEmail(resultSet.getString("email"));
                    userInfo.setDateOfBirth(resultSet.getDate("date_of_birth"));
                    userInfo.setAddress(resultSet.getString("address"));
                    return userInfo;
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting user info", e);
        }
        return null;
    }
    
     public static void updateUserInfo(UserInfo userInfo) {
        String query = "UPDATE user_profile SET full_name = ?, email = ?, date_of_birth = ?, address = ? WHERE user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userInfo.getFullName());
            preparedStatement.setString(2, userInfo.getEmail());
            preparedStatement.setDate(3, userInfo.getDateOfBirth());
            preparedStatement.setString(4, userInfo.getAddress());
            preparedStatement.setInt(5, userInfo.getUserId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Updated User Info Successfully");
            } else {
                logger.warn("No rows were affected by the update");
            }
            connection.commit(); // Commit the transaction
        } catch (SQLException e) {
            logger.error("Error updating user info", e);
        }
    }
    
     public static boolean deleteUser(int userId) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String deleteQuery = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, userId);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            logger.error("Error updating user info", e);
        }
        return false;
    }

    public static double getWalletAmountForCurrentUser(int userId) {
        String query = "SELECT wallet_balance FROM users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("wallet_balance");
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting wallet amount for current user", e);
        }
        return 0.0; // Default wallet balance if not found or error occurs
    }

    public static boolean deductAmountFromWallet(int userId, double bookingAmount) {
        String updateQuery = "UPDATE users SET wallet_balance = wallet_balance - ? WHERE id = ?";

        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setDouble(1, bookingAmount);
            preparedStatement.setInt(2, userId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                connection.commit(); // Commit the transaction
                return true; // Operation successful
            }
        } catch (SQLException e) {
            logger.error("Error deducting amount from wallet", e);
        }
        return false; // Operation unsuccessful
    }
    
    public static boolean saveBooking(int userId, String movieName, String bookingDate, String bookingTime, String seats, double totalAmount, String paymentMode) {
        String query = "INSERT INTO bookings (user_id, movie_name, booking_date, booking_time, seats, total_amount, payment_mode) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, movieName);
            preparedStatement.setString(3, bookingDate);
            preparedStatement.setString(4, bookingTime);
            preparedStatement.setString(5, seats);
            preparedStatement.setDouble(6, totalAmount);
            preparedStatement.setString(7, paymentMode);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                connection.commit(); // Commit the transaction
                logger.info("Booking saved successfully - User ID: {}, Movie: {}, Date: {}, Time: {}, Seats: {}", userId, movieName, bookingDate, bookingTime, seats);
                return true; // Booking saved successfully
            }
        } catch (SQLException e) {
            logger.error("Error saving booking", e);
        }
        return false; // Booking not saved
    }
    
    public static boolean updateUserProfile(int userID, String fullName, String email, String dob, String address) {
        // SQL query to insert data into user_profile table
        String query = "INSERT INTO user_profile (user_id, full_name, email, date_of_birth, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            LocalDate dateOfBirth = LocalDate.parse(dob); // Set values for the PreparedStatement
            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, fullName);
            preparedStatement.setString(3, email);
            preparedStatement.setObject(4, dateOfBirth);
            preparedStatement.setString(5, address);
            // Execute the insert query
            int rowsAffected = preparedStatement.executeUpdate();
            // Check if any rows were affected
            return rowsAffected > 0; // Insert successful
        } catch (SQLException e) {
            // Handle any SQL exceptions
            logger.error("Error inserting user profile", e);
            return false; // Insert failed
        }
    }
    
    public static boolean updateProfileImageIndex(int userId, int profileImageIndex) {
        String query = "UPDATE user_profile SET profileimageindex = ? WHERE user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, profileImageIndex);
            preparedStatement.setInt(2, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            connection.commit(); 
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error updating profile image index", e);
            return false;
        }
    }
    
    public static int getProfileImageIndex(int userId) {
        String query = "SELECT profileimageindex FROM user_profile WHERE user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("profileimageindex");
            }
        } catch (SQLException e) {
            logger.error("Error fetching profile image index", e);
        }
        return 0; // Default value
    }


}
