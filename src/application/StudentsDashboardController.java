package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentsDashboardController implements Initializable {

    @FXML
    private Button createButton;

    @FXML
    private Button readButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelDashboard;

    @FXML
    private Button logoutDashboard;

    @FXML
    private TableView<Students> userTable;

    @FXML
    private TableColumn<Students, String> nameColumn;

    @FXML
    private TableColumn<Students, String> fatherNameColumn;

    @FXML
    private TableColumn<Students, String> surnameColumn;

    @FXML
    private TableColumn<Students, LocalDate> birthdayColumn;

    @FXML
    private TableColumn<Students, String> phoneColumn;

    @FXML
    private TableColumn<Students, String> emailColumn;

    @FXML
    private TableColumn<Students, String> studentclassColumn;

    private StudentsDAO userDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize DAO for database operations
        userDAO = new StudentsDAO();

        // Initialize TableView columns with appropriate cell value factories
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        fatherNameColumn.setCellValueFactory(new PropertyValueFactory<>("fatherName"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentclassColumn.setCellValueFactory(new PropertyValueFactory<>("studentClass"));

        // Fetch data from database and populate TableView
        try {
            List<Students> usersList = userDAO.getAllUsers();
            userTable.getItems().addAll(usersList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch users from database.");
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelDashboardAction(ActionEvent event) {
        // Close the current stage (Dashboard)
        Stage stage = (Stage) cancelDashboard.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void logoutDashboardAction(ActionEvent event) {
        try {
            // Load the Login.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent loginRoot = fxmlLoader.load();

            // Create a new stage for the login screen
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(loginRoot));

            // Close the current stage (Dashboard)
            Stage currentStage = (Stage) logoutDashboard.getScene().getWindow();
            currentStage.close();

            // Show the login stage
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Login window.");
        }
    }

    @FXML
    private void CreateDashboardAction(ActionEvent event) {
        // Dialog for creating a new student
        Dialog<Students> dialog = new Dialog<>();
        dialog.setTitle("Create Student");

        // Set the button types (Create and Cancel)
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        // Create form fields for student details
        TextField nameField = new TextField();
        TextField fatherNameField = new TextField();
        TextField surnameField = new TextField();
        DatePicker birthdayPicker = new DatePicker();
        TextField phoneField = new TextField();
        TextField emailField = new TextField();
        TextField classField = new TextField();

        // GridPane to arrange form fields
        GridPane grid = new GridPane();
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Father's Name:"), 0, 1);
        grid.add(fatherNameField, 1, 1);
        grid.add(new Label("Surname:"), 0, 2);
        grid.add(surnameField, 1, 2);
        grid.add(new Label("Birthday:"), 0, 3);
        grid.add(birthdayPicker, 1, 3);
        grid.add(new Label("Phone:"), 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(new Label("Email:"), 0, 5);
        grid.add(emailField, 1, 5);
        grid.add(new Label("Class:"), 0, 6);
        grid.add(classField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the name field by default
        Platform.runLater(nameField::requestFocus);

        // Convert the result to a student object when the create button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                // Validate fields
                if (nameField.getText().isEmpty() || fatherNameField.getText().isEmpty() ||
                        surnameField.getText().isEmpty() || birthdayPicker.getValue() == null ||
                        phoneField.getText().isEmpty() || emailField.getText().isEmpty() ||
                        classField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Incomplete Fields", "Please fill in all fields.");
                    return null;
                }

                // All fields filled, return new Student object
                return new Students(0, nameField.getText(), fatherNameField.getText(), surnameField.getText(),
                        birthdayPicker.getValue(), phoneField.getText(), emailField.getText(), classField.getText());
            }
            return null;
        });

        // Show the dialog and wait for user response
        Optional<Students> result = dialog.showAndWait();
        result.ifPresent(newStudent -> {
            try {
                userDAO.addUser(newStudent); // Add student to the database
                userTable.getItems().add(newStudent); // Add student to TableView
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add student.");
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void ReadDashboardAction(ActionEvent event) {
        // Show details of the selected student
        Students selectedStudent = userTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            showAlert(Alert.AlertType.INFORMATION, "Student Details",
                    "Name: " + selectedStudent.getName() + "\n" +
                            "Father's Name: " + selectedStudent.getFatherName() + "\n" +
                            "Surname: " + selectedStudent.getSurname() + "\n" +
                            "Birthday: " + selectedStudent.getBirthday() + "\n" +
                            "Phone: " + selectedStudent.getPhone() + "\n" +
                            "Email: " + selectedStudent.getEmail() + "\n" +
                            "Class: " + selectedStudent.getStudentClass());
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No student selected. Please select a student to view details.");
        }
    }

    @FXML
    private void UpdateDashboardAction(ActionEvent event) {
        // Update details of the selected student
        Students selectedStudent = userTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            // Dialog for updating student details
            updateStudentDialog(selectedStudent);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No student selected. Please select a student to update.");
        }
    }

    /**
     * Opens a dialog to update details of the selected student.
     * @param selectedStudent The selected Students object to update.
     */
    private void updateStudentDialog(Students selectedStudent) {
        Dialog<Students> dialog = new Dialog<>();
        dialog.setTitle("Update Student");
        dialog.setHeaderText("Update Details for " + selectedStudent.getName());

        // Set the button types (Update and Cancel)
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create form fields for updating student details
        TextField nameField = new TextField(selectedStudent.getName());
        TextField fatherNameField = new TextField(selectedStudent.getFatherName());
        TextField surnameField = new TextField(selectedStudent.getSurname());
        DatePicker birthdayPicker = new DatePicker(selectedStudent.getBirthday());
        TextField phoneField = new TextField(selectedStudent.getPhone());
        TextField emailField = new TextField(selectedStudent.getEmail());
        TextField classField = new TextField(selectedStudent.getStudentClass());

        GridPane grid = new GridPane();
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Father's Name:"), 0, 1);
        grid.add(fatherNameField, 1, 1);
        grid.add(new Label("Surname:"), 0, 2);
        grid.add(surnameField, 1, 2);
        grid.add(new Label("Birthday:"), 0, 3);
        grid.add(birthdayPicker, 1, 3);
        grid.add(new Label("Phone:"), 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(new Label("Email:"), 0, 5);
        grid.add(emailField, 1, 5);
        grid.add(new Label("Class:"), 0, 6);
        grid.add(classField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a student object when the update button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                // Validate fields
                if (nameField.getText().isEmpty() || fatherNameField.getText().isEmpty() ||
                        surnameField.getText().isEmpty() || birthdayPicker.getValue() == null ||
                        phoneField.getText().isEmpty() || emailField.getText().isEmpty() ||
                        classField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Incomplete Fields", "Please fill in all fields.");
                    return null;
                }

                // Update selected student object
                selectedStudent.setName(nameField.getText());
                selectedStudent.setFatherName(fatherNameField.getText());
                selectedStudent.setSurname(surnameField.getText());
                selectedStudent.setBirthday(birthdayPicker.getValue());
                selectedStudent.setPhone(phoneField.getText());
                selectedStudent.setEmail(emailField.getText());
                selectedStudent.setStudentClass(classField.getText());

                return selectedStudent;
            }
            return null;
        });

        // Show the dialog and wait for user response
        Optional<Students> result = dialog.showAndWait();
        result.ifPresent(updatedStudent -> {
            try {
                userDAO.updateUser(updatedStudent); // Update student in the database
                userTable.refresh(); // Refresh TableView to reflect changes
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update student.");
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void DeleteDashboardAction(ActionEvent event) {
        // Delete the selected student
        Students selectedStudent = userTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            // Confirmation dialog for deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Student");
            alert.setHeaderText("Delete " + selectedStudent.getName() + "?");
            alert.setContentText("Are you sure you want to delete this student?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    userDAO.deleteUser(selectedStudent.getId()); // Delete student from database
                    userTable.getItems().remove(selectedStudent); // Remove student from TableView
                    showAlert(Alert.AlertType.INFORMATION, "Student Deleted", "Student deleted successfully.");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete student.");
                    e.printStackTrace();
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No student selected. Please select a student to delete.");
        }
    }

    @FXML
    private void signupDashboardAction(ActionEvent event) {
        try {
            // Close the current stage (Login)
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
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Add Student window.");
        }
    }

    @FXML
    private void loginDashboardAction(ActionEvent event) {
        try {
            // Close the current stage (Login)
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Load the AddStudent.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent addStudentRoot = fxmlLoader.load();

            // Create a new stage for the Add Student screen
            Stage addStudentStage = new Stage();
            addStudentStage.setTitle("Add Student");
            addStudentStage.setScene(new Scene(addStudentRoot));

            // Show the Add Student stage
            addStudentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Add Student window.");
        }
    }
    /**
     * Helper method to show an alert dialog.
     * @param type The type of alert (e.g., INFORMATION, WARNING, ERROR).
     * @param title The title of the alert dialog.
     * @param message The message to display in the alert dialog.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}





