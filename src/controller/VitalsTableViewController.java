package controller;

import db.VitalsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Patient;
import model.Vital;

import java.util.List;

public class VitalsTableViewController {

    @FXML private TableView<Vital> vitalsTable;
    @FXML private TableColumn<Vital, Double> heartRateCol;
    @FXML private TableColumn<Vital, Double> bloodPressureCol;
    @FXML private TableColumn<Vital, Double> temperatureCol;
    @FXML private TableColumn<Vital, Double> oxygenLevelCol;

    private Patient patient;

    // Called by dashboard to set patient and refresh table
    public void setPatient(Patient patient) {
        this.patient = patient;
        loadVitals();
    }

    public void initialize() {
        // Bind table columns to Vital properties
        heartRateCol.setCellValueFactory(new PropertyValueFactory<>("heartRate"));
        bloodPressureCol.setCellValueFactory(new PropertyValueFactory<>("bloodPressure"));
        temperatureCol.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        oxygenLevelCol.setCellValueFactory(new PropertyValueFactory<>("oxygenLevel"));
    }

    private void loadVitals() {
        if (patient == null) return;
        // Fetch and display vitals for this patient
        List<Vital> vitalsList = VitalsDAO.getVitalsByPatientId(patient.getUserId());
        ObservableList<Vital> observableVitals = FXCollections.observableArrayList(vitalsList);
        vitalsTable.setItems(observableVitals);
    }
}
