package db;

import model.Vital;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Functions to update and get the information from the database according to required actions for vitals

public class VitalsDAO {

    public static void addVital(Vital vital, String patientId, int vitalId) {
        String sql = "INSERT INTO vitals (vital_id, patient_id, heart_rate, blood_pressure, temperature, oxygen_level, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vitalId);
            stmt.setString(2, patientId);
            stmt.setDouble(3, vital.getHeartRate());
            stmt.setDouble(4, vital.getBloodPressure());
            stmt.setDouble(5, vital.getTemperature());
            stmt.setDouble(6, vital.getOxygenLevel());
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error adding vital: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Vital> getVitalsByPatientId(String patientId) {
        List<Vital> vitalsList = new ArrayList<>();
        String sql = "SELECT * FROM vitals WHERE patient_id = ? ORDER BY timestamp ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vital vital = new Vital(
                        rs.getDouble("heart_rate"),
                        rs.getDouble("blood_pressure"),
                        rs.getDouble("temperature"),
                        rs.getDouble("oxygen_level")
                );
                vital.setTimestamp(rs.getTimestamp("timestamp").toString());
                vitalsList.add(vital);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching vitals: " + e.getMessage());
            e.printStackTrace();
        }

        return vitalsList;
    }

    public static List<Vital> getAllVitals() {
        List<Vital> vitals = new ArrayList<>();
        String sql = "SELECT heart_rate, blood_pressure, temperature, oxygen_level, timestamp FROM vitals ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vital v = new Vital(
                        rs.getDouble("heart_rate"),
                        rs.getDouble("blood_pressure"),
                        rs.getDouble("temperature"),
                        rs.getDouble("oxygen_level")
                );
                v.setTimestamp(rs.getTimestamp("timestamp").toString());
                vitals.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vitals;
    }

    public static int getMaxVitalIdGlobal() {
        String sql = "SELECT MAX(vital_id) FROM vitals";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting global max vital ID: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
