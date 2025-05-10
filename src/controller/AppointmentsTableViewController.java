package controller;

import db.AppointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Patient;

import java.util.List;

public class AppointmentsTableViewController {

    @FXML
    private TableView<Appointment> appointmentsTable;     // Table to display patient's appointments

    @FXML
    private TableColumn<Appointment, String> idCol;        // Column for appointment ID

    @FXML
    private TableColumn<Appointment, String> dateTimeCol;  // Column for formatted date/time

    @FXML
    private TableColumn<Appointment, String> statusCol;    // Column for appointment status

    private Patient patient;                               // Current patient context

    // Called externally to set the patient and immediately load their appointments
    public void setPatient(Patient patient) {
        this.patient = patient;
        loadAppointments();
    }

    public void initialize() {
        // Link columns to Appointment properties
        idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("formattedDateTime"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadAppointments() {
        if (patient == null) return;
        List<Appointment> list = AppointmentDAO.getAppointmentsByPatientId(patient.getUserId());
        ObservableList<Appointment> observableList = FXCollections.observableArrayList(list);
        appointmentsTable.setItems(observableList);
    }
}
