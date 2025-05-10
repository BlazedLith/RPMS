package db;

import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Functions to update and get the information from the database according to required actions for admins

public class AdminDAO {
    public static List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, name, email, password, role FROM users";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("user_id"),
                        name = rs.getString("name"),
                        email = rs.getString("email"),
                        pwd = rs.getString("password"),
                        role = rs.getString("role");
                switch (role) {
                    case "patient":
                        list.add(new model.Patient(id, name, email, pwd));
                        break;
                    case "doctor":
                        list.add(new model.Doctor(id, name, email, pwd));
                        break;
                    default:
                        list.add(new model.Administrator(id, name, email, pwd));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE user_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addUser(String userId, String name, String email, String password, String role) {
        String sql = "INSERT INTO users (user_id, name, email, password, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, role.toLowerCase());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void linkPatientToDoctor(String patientId, String doctorId) {
        String sql = "INSERT INTO patient_doctor (patient_id, doctor_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId);
            stmt.setString(2, doctorId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}