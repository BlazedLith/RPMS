package db;

import model.Appointment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Functions to update and get the information from the database according to required actions for appointments

public class AppointmentDAO {

    public static List<Appointment> getAppointmentsByPatientId(String patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT appointment_id, date_time, status FROM appointments WHERE patient_id = ? ORDER BY date_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("appointment_id");
                Timestamp ts = rs.getTimestamp("date_time");
                LocalDateTime dt = ts.toLocalDateTime();
                String status = rs.getString("status");

                appointments.add(new Appointment(id, null, dt, status));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching appointments: " + e.getMessage());
            e.printStackTrace();
        }

        return appointments;
    }

    public static String generateNextAppointmentId() {
        String prefix = "a";
        String sql = "SELECT appointment_id FROM appointments ORDER BY appointment_id DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("appointment_id");
                int num = Integer.parseInt(lastId.substring(prefix.length()));
                return prefix + String.format("%03d", num + 1);
            } else {
                return prefix + "001";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return prefix + System.currentTimeMillis();
        }
    }

    public static boolean addAppointment(Appointment appt) {
        if (appt.getAppointmentId() == null || appt.getAppointmentId().isEmpty()) {
            appt.setAppointmentId(generateNextAppointmentId());
        }

        String sql = "INSERT INTO appointments (appointment_id, patient_id, date_time, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appt.getAppointmentId());
            stmt.setString(2, appt.getPatient().getUserId());
            stmt.setTimestamp(3, Timestamp.valueOf(appt.getDateTime()));
            stmt.setString(4, appt.getStatus());

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateAppointmentStatus(String appointmentId, String newStatus) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setString(2, appointmentId);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.date_time, a.status, u.name, u.email\n" +
                "FROM appointments a\n" +
                "JOIN users u ON a.patient_id = u.user_id\n" +
                "ORDER BY a.date_time DESC\n";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("appointment_id");
                String patientName = rs.getString("name");
                String patientEmail = rs.getString("email");
                String patientId = rs.getString("patient_id");
                LocalDateTime dateTime = rs.getTimestamp("date_time").toLocalDateTime();
                String status = rs.getString("status");

                Appointment appt = new Appointment(id, new model.Patient(patientId, patientName, patientEmail, ""), dateTime, status);
                appointments.add(appt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
}
