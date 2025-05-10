package db;

import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Functions to update and get the information from the database according to required actions for users

public class UserDAO {

    public static User getUserByEmailAndPassword(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String userId = rs.getString("user_id");
                String name = rs.getString("name");
                String role = rs.getString("role");

                switch (role.toLowerCase()) {
                    case "patient":
                        return new Patient(userId, name, email, password);
                    case "doctor":
                        return new Doctor(userId, name, email, password);
                    case "admin":
                        return new Administrator(userId, name, email, password);
                    default:
                        System.err.println("Unknown role: " + role);
                }
            }

        } catch (Exception e) {
            System.err.println("Error during user login: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static List<Patient> getPatientsByDoctor(String doctorId) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'patient'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Patient p = new Patient(
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                patients.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return patients;
    }

    public static String getDoctorEmailByPatientId(String patientId) {
        String sql = "SELECT u.email FROM users u " +
                "JOIN patient_doctor pd ON u.user_id = pd.doctor_id " +
                "WHERE pd.patient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAssignedDoctorId(String patientId) {
        String sql = "SELECT doctor_id FROM patient_doctor WHERE patient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("doctor_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getEmailByUserId(String userId) {
        String email = null;
        String sql = "SELECT email FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                email = rs.getString("email");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return email;
    }
}
