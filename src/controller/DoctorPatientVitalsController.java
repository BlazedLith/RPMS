package controller;

import db.VitalsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Vital;

import java.util.List;

public class DoctorPatientVitalsController {

    @FXML private TableView<Vital> vitalsTable;
    @FXML private TableColumn<Vital, Double> heartRateCol;
    @FXML private TableColumn<Vital, Double> bpCol;
    @FXML private TableColumn<Vital, Double> tempCol;
    @FXML private TableColumn<Vital, Double> oxygenCol;
    @FXML private TableColumn<Vital, String> timeCol;

    public void initialize() {
        // Bind table columns to Vital properties
        heartRateCol.setCellValueFactory(new PropertyValueFactory<>("heartRate"));
        bpCol.setCellValueFactory(new PropertyValueFactory<>("bloodPressure"));
        tempCol.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        oxygenCol.setCellValueFactory(new PropertyValueFactory<>("oxygenLevel"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        loadVitals();
    }

    private void loadVitals() {
        // Fetch all vitals (or filter by patient) and populate the table
        List<Vital> vitals = VitalsDAO.getAllVitals();
        ObservableList<Vital> data = FXCollections.observableArrayList(vitals);
        vitalsTable.setItems(data);
    }
}
