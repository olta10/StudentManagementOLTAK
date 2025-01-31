package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button registerButton;

    @FXML
    private void loginButtonAction(ActionEvent event) {
        String userEmail = emailField.getText();
        String userPass = passwordField.getText();

        // Validate input fields
        if (userEmail.isEmpty() || userPass.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection(); // Establish database connection
            System.out.println("Database connection established.");

            // Check if user exists with the given email and password
            stmt = conn.prepareStatement("SELECT * FROM admins WHERE email = ? AND password = ?");
            stmt.setString(1, userEmail);
            stmt.setString(2, userPass);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // User found in the database
                System.out.println("User found in the database.");
                boolean isAdmin = rs.getBoolean("is_admin");

                if (isAdmin) {
                    // User is an admin
                    System.out.println("User is an admin.");
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.close();

                    // Load AdminDashboard.fxml
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
                    Parent adminDashboardRoot = fxmlLoader.load();

                    // Create a new stage for Admin Dashboard
                    Stage adminDashboardStage = new Stage();
                    adminDashboardStage.setTitle("Admin Dashboard");
                    adminDashboardStage.setScene(new Scene(adminDashboardRoot));

                    // Show Admin Dashboard
                    adminDashboardStage.show();
                } else {
                    // Regular users are not allowed to access the dashboard
                    showAlert(Alert.AlertType.ERROR, "Access Denied", "Regular users are not allowed to access the dashboard.");

                    // Close the application
                    Stage stage = (Stage) cancelButton.getScene().getWindow();
                    stage.close();
                }

            } else {
                // User not found, add a new user
                System.out.println("User not found, adding new user.");
                stmt = conn.prepareStatement("INSERT INTO admins (email, password, is_admin) VALUES (?, ?, ?)");
                stmt.setString(1, userEmail);
                stmt.setString(2, userPass);
                stmt.setBoolean(3, false);

                int rowsAffected = stmt.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);

                if (rowsAffected > 0) {
                    // User added successfully
                    System.out.println("User added successfully.");
                    showAlert(Alert.AlertType.INFORMATION, "User Added", "User added successfully!");

                    // Close the application
                    Stage stage = (Stage) cancelButton.getScene().getWindow();
                    stage.close();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Registration Error", "Failed to add user.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to log in or register: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "FXML Error", "Failed to load FXML file: " + e.getMessage());
        } finally {
            // Close resources in finally block
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        // Close the application when Cancel button is clicked
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addStudentButtonAction(ActionEvent event) {
        try {
            // Close the current stage (Login)
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Load AddStudent.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddStudent.fxml"));
            Parent addStudentRoot = fxmlLoader.load();

            // Create a new stage for Add Student screen
            Stage addStudentStage = new Stage();
            addStudentStage.setTitle("Add Student");
            addStudentStage.setScene(new Scene(addStudentRoot));

            // Show Add Student stage
            addStudentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Add Student window.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        // Helper method to show an alert dialog with the given type, title, and message
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
