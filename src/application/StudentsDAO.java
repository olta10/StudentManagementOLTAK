package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentsDAO {
	
    /**
     * Deletes a student from the database by their ID.
     *
     * @param id The ID of the student to be deleted.
     * @throws SQLException if a database access error occurs or the SQL query is invalid.
     */

    public List<Students> getAllUsers() throws SQLException {
        List<Students> users = new ArrayList<>();

        String query = "SELECT * FROM students";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String fatherName = resultSet.getString("fatherName");
                String surname = resultSet.getString("surname");
                LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String studentClass = resultSet.getString("studentClass");

                Students user = new Students(id, name, fatherName, surname, birthday, phone, email, studentClass);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception to be handled by the caller
        }

        return users;
    }

    public void addUser(Students user) throws SQLException {
        String query = "INSERT INTO students (name, fatherName, surname, birthday, phone, email, studentClass) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getFatherName());
            preparedStatement.setString(3, user.getSurname());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));
            preparedStatement.setString(5, user.getPhone());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getStudentClass());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception to be handled by the caller
        }
    }

    public void updateUser(Students user) throws SQLException {
        String query = "UPDATE students SET name = ?, fatherName = ?, surname = ?, birthday = ?, phone = ?, email = ?, studentClass = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getFatherName());
            preparedStatement.setString(3, user.getSurname());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));
            preparedStatement.setString(5, user.getPhone());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getStudentClass());
            preparedStatement.setInt(8, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception to be handled by the caller
        }
    }

    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM students WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception to be handled by the caller
        }
    }
}
