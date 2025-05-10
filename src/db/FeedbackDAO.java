package db;

import model.Feedback;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Functions to update and get the information from the database according to required actions for Feedbacks

public class FeedbackDAO {

    public static List<Feedback> getFeedbackByPatientId(String patientId) {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE patient_id = ? ORDER BY date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Feedback feedback = new Feedback(
                        rs.getString("feedback_id"),
                        rs.getString("doctor_id"),
                        rs.getString("patient_id"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        rs.getString("comments"),
                        Arrays.asList(rs.getString("medications").split(",\s*"))
                );
                feedbackList.add(feedback);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching feedback: " + e.getMessage());
            e.printStackTrace();
        }

        return feedbackList;
    }

    public static boolean saveFeedback(Feedback feedback) {
        String sql = "INSERT INTO feedback (feedback_id, doctor_id, patient_id, date, comments, medications) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, feedback.getFeedbackId());
            stmt.setString(2, feedback.getDoctorId());
            stmt.setString(3, feedback.getPatientId());
            stmt.setTimestamp(4, Timestamp.valueOf(feedback.getDate()));
            stmt.setString(5, feedback.getComments());
            stmt.setString(6, String.join(", ", feedback.getMedications()));

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Feedback> getFeedbackByDoctorId(String doctorId) {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE doctor_id = ? ORDER BY date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Feedback fb = new Feedback(
                        rs.getString("feedback_id"),
                        rs.getString("doctor_id"),
                        rs.getString("patient_id"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        rs.getString("comments"),
                        Arrays.asList(rs.getString("medications").split(",\\s*"))
                );
                feedbackList.add(fb);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feedbackList;
    }

    public static List<Feedback> getAllFeedback() {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM feedback";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Feedback f = new Feedback(
                        rs.getString("feedback_id"),
                        rs.getString("doctor_id"),
                        rs.getString("patient_id"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        rs.getString("comments"),
                        Arrays.asList(rs.getString("medications").split(",\\s*"))
                );
                list.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
