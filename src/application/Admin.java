package application;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Admin {
    private int id;
    private final StringProperty email;
    private final StringProperty password;
    private final SimpleBooleanProperty isAdmin;

    // Constructor with all parameters
    public Admin(int id, String email, String password, boolean isAdmin) {
        this.id = id;
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.isAdmin = new SimpleBooleanProperty(isAdmin);
    }

    // Constructor without ID (for new instances)
    public Admin(String email, String password, boolean isAdmin) {
        this(0, email, password, isAdmin); // Default ID is 0 for new records
    }

    // Getters and setters for ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters for email using StringProperty
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    // Property accessor for email
    public StringProperty emailProperty() {
        return email;
    }

    // Getters and setters for password using StringProperty
    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    // Property accessor for password
    public StringProperty passwordProperty() {
        return password;
    }

    // Getters and setters for isAdmin using SimpleBooleanProperty
    public boolean isAdmin() {
        return isAdmin.get();
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin.set(isAdmin);
    }

    // Property accessor for isAdmin
    public SimpleBooleanProperty isAdminProperty() {
        return isAdmin;
    }
}
