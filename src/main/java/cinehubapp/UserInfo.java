package cinehubapp;

import java.sql.Date;

public class UserInfo {
    private int id;
    private int userId;
    private String fullName;
    private String email;
    private Date dateOfBirth; // Change to java.sql.Date
    private String address;
    
    private static final int MAX_PROFILE_IMAGE_INDEX = 20;
    public int profileImageIndex = UserProfileWindow.selectedProfileIndex;
    private int selectedProfileIndex = UserProfileWindow.selectedProfileIndex;

    // Constructors
    public UserInfo() {
        // Default constructor
    }

    public UserInfo(int id, int userId, String fullName, String email, Date dateOfBirth, String address) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    // Getter and Setter methods

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public int getProfileImageIndex() {
        return profileImageIndex;
    }
    
    public int getMaxProfileImageIndex() {
        return MAX_PROFILE_IMAGE_INDEX;
    }
    
    public void setProfileImageIndex(int profileImageIndex) {
        this.profileImageIndex = profileImageIndex;
    }

    public void decrementProfileImageIndex() {
        // Ensure the index is within valid range
        if (profileImageIndex > 0) {
            profileImageIndex--;
        }
        setSelectedProfileIndex(profileImageIndex);
    }

    public void incrementProfileImageIndex() {
        // Increment the index
        profileImageIndex++;
        // Ensure the index does not exceed the maximum number of profile images
        // Assuming you have a constant or method getMaxProfileImageIndex() to get the maximum index
        if (profileImageIndex >= getMaxProfileImageIndex()) {
            profileImageIndex = getMaxProfileImageIndex() - 1;
        }
        setSelectedProfileIndex(profileImageIndex);
    }
    
    public int getSelectedProfileIndex() {
        return selectedProfileIndex;
    }
    
    public void setSelectedProfileIndex(int selectedProfileIndex) {
        this.selectedProfileIndex = selectedProfileIndex;
    }
}
