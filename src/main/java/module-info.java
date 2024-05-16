module cinehubapp {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.sql; // Add this line to declare the dependency on the java.sql module

    opens cinehubapp to javafx.fxml;
    exports cinehubapp;
    requires javafx.media;
    requires java.mail;
    requires org.slf4j;
}
