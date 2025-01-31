package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    // Retrieve all admins from the database
    public List<Admin> getAllAdmins() throws SQLException {
        List<Admin> admins = new ArrayList<>();
        String query = "SELECT * FROM admins";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Admin admin = new Admin(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getBoolean("is_admin")
                );
                admins.add(admin);
            }
        }

        return admins;
    }

    // Add a new admin to the database
    public void addAdmin(Admin admin) throws SQLException {
        String query = "INSERT INTO admins (email, password, is_admin) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, admin.getEmail());
            stmt.setString(2, admin.getPassword());
            stmt.setBoolean(3, admin.isAdmin());

            stmt.executeUpdate();

            // Retrieve the auto-generated ID and set it in the Admin object
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                admin.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Failed to retrieve generated ID.");
            }
        }
    }

    // Update an existing admin in the database
    public void updateAdmin(Admin admin) throws SQLException {
        String query = "UPDATE admins SET email = ?, password = ?, is_admin = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, admin.getEmail());
            stmt.setString(2, admin.getPassword());
            stmt.setBoolean(3, admin.isAdmin());
            stmt.setInt(4, admin.getId());

            stmt.executeUpdate();
        }
    }

    // Delete an admin from the database by ID
    public void deleteAdmin(int id) throws SQLException {
        String query = "DELETE FROM admins WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
