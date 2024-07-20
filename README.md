# CineHubApp README Documentation

Overview

CineHubApp is a JavaFX-based application designed to provide a comprehensive platform for movie enthusiasts. It offers features like user authentication, profile management, booking, and more. This documentation outlines the application's structure, functionality, and usage.

Application Structure

The application consists of the following packages and classes:

- cinehubapp: The main package containing all the application's classes.
    - CineHubSplashScreen.java: The splash screen displayed on application launch.
    - DatabaseConnector.java: Manages database connections and provides data access methods.
    - ErrorWindow.java: Displays error messages with a "Try Again" button.
    - FeatureComingSoonWindow.java: Informs users about upcoming features.
    - LoginWindow.java: Handles user authentication and registration.
    - User.java: Represents a user with an ID, username, and password.
    - UserInfo.java: Stores and retrieves user information.
    - SystemInfo.java: Provides methods to retrieve Java and JavaFX versions.

Functionality

1. User Authentication: Users can log in or register using the LoginWindow.
2. Profile Management: Users can update their profile information using the UserInfo class.
3. Booking: Users can book movies (feature coming soon).
4. Error Handling: ErrorWindow displays error messages with a "Try Again" button.
5. Upcoming Features: FeatureComingSoonWindow informs users about new features.

Usage

1. Run the application using the CineHubSplashScreen class.
2. Log in or register using the LoginWindow.
3. Update profile information using the UserInfo class.
4. Book movies (feature coming soon).
5. View error messages and retry actions using the ErrorWindow.
6. Get notified about upcoming features using the FeatureComingSoonWindow.

Database

The application uses a MySQL database to store user information and booking data. The DatabaseConnector class manages connections and provides data access methods.

System Requirements

- Java 11 or later
- JavaFX 11 or later
- MySQL database

Known Issues

- Booking feature is not implemented yet.
- Error handling can be improved.

Future Development

- Implement booking feature.
- Enhance error handling.
- Add more features (e.g., movie reviews, ratings).

Contributing

Contributions are welcome! Please submit pull requests or report issues on the project's GitHub page.

License

CineHubApp is licensed under the MIT License. See LICENSE.txt for details.

# Opening Screen
![Opening Screen](https://github.com/user-attachments/assets/c67c3417-d8aa-4459-816e-bf5254b4af14)
# User Profile Window
![User Profile Window](https://github.com/user-attachments/assets/625fb216-99ca-4cf8-b254-58f0f2465d23)
# Loading Screen
![Loading Screen](https://github.com/user-attachments/assets/e2bfc18b-6a2e-4665-954b-0502fb6febff)
# Booking Screen
![Booking Screen](https://github.com/user-attachments/assets/9935073a-9c33-4001-b921-93796bc1116d)
# Viewing Abailable Movies Window
![Available Movies Scren 3](https://github.com/user-attachments/assets/70c6e645-99dc-4a78-8e68-8cc5875a359e)
![Available Movies Screen 2](https://github.com/user-attachments/assets/1ea8f324-4b47-4ece-839e-4618c11a4a2e)
![Available Movies Screen 1](https://github.com/user-attachments/assets/d2c0aee4-2c17-4d55-93fb-b27b012258e3)
# New Registration Window
![New Registration Window](https://github.com/user-attachments/assets/49b07f4f-5f4c-4a50-a4d2-a9209020d94b)
# Main Screen(Home Screen)
![Main Screen](https://github.com/user-attachments/assets/0ce247f4-2606-4247-bd5b-0c19c609f671)
# Login Screen
![Login Window](https://github.com/user-attachments/assets/049f4175-1af3-4483-af56-b5b7a634175c)
# Forgot Password Window
![Forgot Password Window](https://github.com/user-attachments/assets/b43a4064-05b7-4eee-9195-218f9b550d7d)
