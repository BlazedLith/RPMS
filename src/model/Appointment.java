package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private String appointmentId;
    private Patient patient;
    private LocalDateTime dateTime;
    private String status;

    public String getPatientName() {
        return patient != null ? patient.getName() : "";
    }

    public Appointment(String appointmentId, Patient patient, LocalDateTime dateTime, String status) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.dateTime = dateTime;
        this.status = status;
    }
//Getters and Setters
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getFormattedDateTime() {
        return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
