package controller;

import db.AppointmentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Appointment;
import model.Patient;
import model.UIHelper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RequestAppointmentController {

    @FXML private DatePicker datePicker;   // Choose appointment date
    @FXML private TextField timeField;     // Enter appointment time (HH:mm)

    private Patient patient;

    // Set the current patient context
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @FXML
    private void handleSubmit() {
        LocalDate date = datePicker.getValue();
        String timeText = timeField.getText().trim();

        if (date == null || timeText.isEmpty()) {
            UIHelper.showError("Please select date and enter time (HH:mm).");
            return;
        }

        try {
            // Parse time and combine with date
            String[] parts = timeText.split(":");
            LocalTime time = LocalTime.of(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1])
            );
            LocalDateTime dt = LocalDateTime.of(date, time);

            Appointment appt = new Appointment(null, patient, dt, "scheduled");
            boolean ok = AppointmentDAO.addAppointment(appt);

            if (ok) {
                UIHelper.showSuccess("Appointment requested for "
                        + appt.getFormattedDateTime());
            } else {
                UIHelper.showError("Failed to request appointment. Try again.");
            }
        } catch (Exception e) {
            UIHelper.showError("Invalid time format. Please use HH:mm.");
            e.printStackTrace();
        }
    }
}
