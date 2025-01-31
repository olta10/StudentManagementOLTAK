package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddStudentController {

    private Connection conn = null;
    private PreparedStatement pstmt = null;

    @FXML
    private TextField nameField;
    @FXML
    private TextField fatherNameField;
    @FXML
    private TextField surnameField;
    @FXML
    private DatePicker birthdayField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField studentclassField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    // Constructor
    public AddStudentController() {
        initializeDBConnection();
    }

    // Initialize database connection
    private void initializeDBConnection() {
        try {
            String dbURL = "jdbc:mysql://localhost:3306/student";
            String user = "root";
            String password = "";
            
            // Establish connection
            conn = DriverManager.getConnection(dbURL, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Connection Failed", "Could not connect to the database.");
        }
    }

    // Save button action handler
    @FXML
    private void SaveButtonAction(ActionEvent event) {
        // Retrieve data from input fields
        String name = nameField.getText();
        String fatherName = fatherNameField.getText();
        String surname = surnameField.getText();
        String birthday = (birthdayField.getValue() != null) ? birthdayField.getValue().toString() : null;
        String phone = phoneField.getText();
        String email = emailField.getText();
        String studentClass = studentclassField.getText();

        // Validate input fields
        if (name.isEmpty() || fatherName.isEmpty() || surname.isEmpty() || birthday == null || phone.isEmpty() || email.isEmpty() || studentClass.isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        try {
            // Prepare SQL statement for insertion
            String query = "INSERT INTO students (name, fatherName, surname, birthday, phone, email, studentClass) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, fatherName);
            pstmt.setString(3, surname);
            pstmt.setString(4, birthday);
            pstmt.setString(5, phone);
            pstmt.setString(6, email);
            pstmt.setString(7, studentClass);

            // Execute the SQL update
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Student added successfully.");
                clearFields();
                closeWindow();
            } else {
                showAlert(AlertType.ERROR, "Failed", "Could not add the student.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Exception", "Error occurred while adding student: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Cancel button action handler
    @FXML
    private void CancelButtonAction(ActionEvent event) {
        // Clear input fields
        clearFields();
        
        // Close the window
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    // Clear input fields
    private void clearFields() {
        nameField.clear();
        fatherNameField.clear();
        surnameField.clear();
        birthdayField.setValue(null);
        phoneField.clear();
        emailField.clear();
        studentclassField.clear();
    }

    // Show an alert dialog
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Close window if it's an information alert and OK is clicked
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK && alertType == AlertType.INFORMATION) {
                closeWindow();
            }
        });
    }

    // Close the current window
    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
