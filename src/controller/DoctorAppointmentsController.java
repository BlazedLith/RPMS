package controller;

import db.AppointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.Appointment;
import model.EmailService;

import java.util.List;

public class DoctorAppointmentsController {

    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> idCol;
    @FXML private TableColumn<Appointment, String> patientCol;
    @FXML private TableColumn<Appointment, String> dateTimeCol;
    @FXML private TableColumn<Appointment, String> statusCol;
    @FXML private TableColumn<Appointment, Void> actionCol;

    public void initialize() {
        // Map table columns to Appointment properties
        idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("formattedDateTime"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadAppointments();
    }

    private void loadAppointments() {
        List<Appointment> list = AppointmentDAO.getAllAppointments();
        appointmentsTable.setItems(FXCollections.observableArrayList(list));

        // Add Approve/Reject/Complete buttons per row
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button approveBtn = new Button("Approve");
            private final Button rejectBtn  = new Button("Reject");
            private final Button completeBtn = new Button("Complete");

            {
                approveBtn.setOnAction(e -> updateStatus(getCurrent(), "approved"));
                rejectBtn.setOnAction(e -> updateStatus(getCurrent(), "rejected"));
                completeBtn.setOnAction(e -> updateStatus(getCurrent(), "completed"));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Appointment appt = getCurrent();
                    approveBtn.setDisable(!appt.getStatus().equalsIgnoreCase("scheduled"));
                    rejectBtn.setDisable(!appt.getStatus().equalsIgnoreCase("scheduled"));
                    completeBtn.setDisable(!appt.getStatus().equalsIgnoreCase("approved"));
                    setGraphic(new HBox(5, approveBtn, rejectBtn, completeBtn));
                }
            }

            private Appointment getCurrent() {
                return getTableView().getItems().get(getIndex());
            }
        });
    }

    private void updateStatus(Appointment appt, String newStatus) {
        if (AppointmentDAO.updateAppointmentStatus(appt.getAppointmentId(), newStatus)) {
            appt.setStatus(newStatus);
            appointmentsTable.refresh();

            // Notify patient via email
            String email   = appt.getPatient().getEmail();
            String subject = "Appointment " + newStatus.toUpperCase();
            String body    = "Dear " + appt.getPatient().getName() + ",\n\n"
                    + "Your appointment on " + appt.getFormattedDateTime()
                    + " has been " + newStatus.toUpperCase() + ".\n\nRegards,\nRPMS Team";
            EmailService.sendEmail(email, subject, body);

        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update appointment status.").showAndWait();
        }
    }
}
