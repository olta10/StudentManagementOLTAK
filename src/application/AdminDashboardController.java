package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class AdminDashboardController {

    @FXML
    private Button logoutAdminDashboard;

    @FXML
    private Button cancelAdmin;

    @FXML
    private Button createAdminButton;

    @FXML
    private Button updateAdminButton;

    @FXML
    private Button addAdminButton;

    @FXML
    private Button deleteAdminButton;

    @FXML
    private Button readAdminButton;

    @FXML
    private Button signupAdminDashboard;

    @FXML
    private Button loginAdminDashboard;

    @FXML
    private TableView<Admin> adminTable;

    @FXML
    private TableColumn<Admin, String> adminEmailColumn;

    @FXML
    private TableColumn<Admin, String> adminPasswordColumn;

    private AdminDAO adminDAO;

    public AdminDashboardController() throws SQLException {
        adminDAO = new AdminDAO();
    }

    @FXML
    private void initialize() {
        adminEmailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        adminPasswordColumn.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());

        loadAdmins();
    }

    private void loadAdmins() {
        try {
            adminTable.getItems().setAll(adminDAO.getAllAdmins());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load admins.");
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelAdminAction(ActionEvent event) {
        Stage stage = (Stage) cancelAdmin.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void createAdminAction(ActionEvent event) {
        Admin newAdmin = new Admin("New Admin", "1234567890", false); // Set isAdmin to false
        try {
            adminDAO.addAdmin(newAdmin);
            adminTable.getItems().add(newAdmin);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add admin.");
            e.printStackTrace();
        }
    }

    @FXML
    private void updateAdminAction(ActionEvent event) {
        Admin selectedAdmin = adminTable.getSelectionModel().getSelectedItem();
        if (selectedAdmin != null) {
            try {
                showDialogAndUpdateAdmin(selectedAdmin);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to open update dialog.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No admin selected.");
        }
    }

    private void showDialogAndUpdateAdmin(Admin selectedAdmin) throws IOException {
        Dialog<Admin> dialog = new Dialog<>();
        dialog.setTitle("Update Admin");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        TextField emailField = new TextField(selectedAdmin.getEmail());
        TextField passwordField = new TextField(selectedAdmin.getPassword());

        GridPane grid = new GridPane();
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                selectedAdmin.setEmail(emailField.getText());
                selectedAdmin.setPassword(passwordField.getText());
                return selectedAdmin;
            }
            return null;
        });

        Optional<Admin> result = dialog.showAndWait();
        result.ifPresent(updatedAdmin -> {
            try {
                adminDAO.updateAdmin(updatedAdmin);
                adminTable.refresh(); // Refresh table to reflect changes
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update admin.");
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void addAdminAction(ActionEvent event) {
        createAdminAction(event);
    }

    @FXML
    private void deleteAdminAction(ActionEvent event) {
        Admin selectedAdmin = adminTable.getSelectionModel().getSelectedItem();
        if (selectedAdmin != null) {
            try {
                showDialogAndDeleteAdmin(selectedAdmin);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to open delete dialog.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No admin selected.");
        }
    }

    private void showDialogAndDeleteAdmin(Admin selectedAdmin) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Admin");
        alert.setHeaderText("Are you sure you want to delete this admin?");
        alert.setContentText("ID: " + selectedAdmin.getId() + "\n"
                + "Email: " + selectedAdmin.getEmail() + "\n"
                + "Password: " + selectedAdmin.getPassword());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                adminDAO.deleteAdmin(selectedAdmin.getId());
                adminTable.getItems().remove(selectedAdmin);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete admin.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void readAdminAction(ActionEvent event) {
        Admin selectedAdmin = adminTable.getSelectionModel().getSelectedItem();
        if (selectedAdmin != null) {
            showAlert(Alert.AlertType.INFORMATION, "User Details",
                    "Email: " + selectedAdmin.getEmail() + "\n" +
                            "Password: " + selectedAdmin.getPassword());
        } else {
            System.out.println("No admin selected.");
        }
    }

    @FXML
    private void signupAdminDashboardAction(ActionEvent event) {
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
    private void loginAdminDashboardAction(ActionEvent event) {
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

    private void openNewWindow(String fxmlFile, String title) {
        try {
            Stage currentStage = (Stage) logoutAdminDashboard.getScene().getWindow();
            currentStage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open " + title + " window.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
