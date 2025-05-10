package db;

import model.CommunicationRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Functions to update and get the information from the database according to required actions for Communication Requests

public class CommunicationRequestDAO {

    public static void createRequest(String patientId, String doctorId, String type) {
        String sql = "INSERT INTO communication_requests (request_id, patient_id, doctor_id, type, status, link) " +
                "VALUES (?, ?, ?, ?, 'pending', '')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String requestId = UUID.randomUUID().toString().substring(0, 8);

            stmt.setString(1, requestId);
            stmt.setString(2, patientId);
            stmt.setString(3, doctorId);
            stmt.setString(4, type);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<CommunicationRequest> getRequestsByPatient(String patientId) {
        List<CommunicationRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM communication_requests WHERE patient_id = ? ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new CommunicationRequest(
                        rs.getString("request_id"),
                        rs.getString("patient_id"),
                        rs.getString("doctor_id"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("link")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<CommunicationRequest> getRequestsByDoctor(String doctorId) {
        List<CommunicationRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM communication_requests WHERE doctor_id = ? ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new CommunicationRequest(
                        rs.getString("request_id"),
                        rs.getString("patient_id"),
                        rs.getString("doctor_id"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("link")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean respondToRequest(String requestId, String status, String link) {
        String sql = "UPDATE communication_requests SET status = ?, link = ? WHERE request_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, link);
            stmt.setString(3, requestId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
