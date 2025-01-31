package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private ImageView addStudentImage;
    @FXML
    private Button addStudents;

    @FXML
    private ImageView viewStudentsImage;
    @FXML
    private Button showStudents;

    @FXML
    private ImageView addAdminImage;
    @FXML
    private Button addAdmin;

    @FXML
    private ImageView viewAdminsImage;
    @FXML
    private Button showAdmin;

    @FXML
    private Button cancelHome;

    @FXML
    private void CancelHomeAction(ActionEvent event) {
        // Close the current stage (window) when Cancel button is clicked
        Stage stage = (Stage) cancelHome.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void initialize() {
        // Initialize images for buttons when the controller is loaded
        addStudentImage.setImage(new Image(getClass().getResourceAsStream("/images/addStudent.png")));
        viewStudentsImage.setImage(new Image(getClass().getResourceAsStream("/images/showStudents.png")));
        addAdminImage.setImage(new Image(getClass().getResourceAsStream("/images/addAdmin.png")));
        viewAdminsImage.setImage(new Image(getClass().getResourceAsStream("/images/showAdmins.png")));
    }

    @FXML
    private void addStudentsAction(ActionEvent event) {
        try {
            // Close the current stage (window)
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Load the AddStudent.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddStudent.fxml"));
            Parent addStudentRoot = fxmlLoader.load();

            // Create a new stage for the Add Student screen
            Stage addStudentStage = new Stage();
            addStudentStage.setTitle("Add Student");
            addStudentStage.setScene(new Scene(addStudentRoot));

            // Show the Add Student stage
            addStudentStage.show();
        } catch (IOException e) {
            // Show an error alert if failed to open Add Student window
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Add Student window.");
            e.printStackTrace();
        }
    }

    @FXML
    private void showStudentsAction(ActionEvent event) {
        try {
            // Close the current stage (window)
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Load the StudentsDashboard.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StudentsDashboard.fxml"));
            Parent studentsDashboardRoot = fxmlLoader.load();

            // Create a new stage for the Students Dashboard screen
            Stage studentsDashboardStage = new Stage();
            studentsDashboardStage.setTitle("Students Dashboard");
            studentsDashboardStage.setScene(new Scene(studentsDashboardRoot));

            // Show the Students Dashboard stage
            studentsDashboardStage.show();
        } catch (IOException e) {
            // Show an error alert if failed to open Students Dashboard window
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Students Dashboard window.");
            e.printStackTrace();
        }
    }

    @FXML
    private void addAdminAction(ActionEvent event) {
        try {
            // Close the current stage (window)
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Load the Login.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent loginRoot = fxmlLoader.load();

            // Create a new stage for the Login screen
            Stage loginStage = new Stage();
            loginStage.setTitle("Admin Login");
            loginStage.setScene(new Scene(loginRoot));

            // Show the Login stage
            loginStage.show();
        } catch (IOException e) {
            // Show an error alert if failed to open Admin Login window
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Admin Login window.");
            e.printStackTrace();
        }
    }

    @FXML
    private void showAdminAction(ActionEvent event) {
        try {
            // Close the current stage (window)
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Load the AdminDashboard.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
            Parent adminDashboardRoot = fxmlLoader.load();

            // Create a new stage for the Admin Dashboard screen
            Stage adminDashboardStage = new Stage();
            adminDashboardStage.setTitle("Admin Dashboard");
            adminDashboardStage.setScene(new Scene(adminDashboardRoot));

            // Show the Admin Dashboard stage
            adminDashboardStage.show();
        } catch (IOException e) {
            // Show an error alert if failed to open Admin Dashboard window
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Admin Dashboard window.");
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        // Helper method to show an alert dialog with the given type, title, and message
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
