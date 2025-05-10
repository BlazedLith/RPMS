package controller;

import db.VitalsDAO;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import model.Patient;
import model.Vital;

import java.util.List;

public class VitalsTrendViewController {

    @FXML private LineChart<String, Number> lineChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    private Patient patient;

    // Called by dashboard to pass the selected patient
    public void setPatient(Patient patient) {
        this.patient = patient;
        loadVitalsData();
    }

    // Fetches vitals and populates the chart
    private void loadVitalsData() {
        if (patient == null) return;

        List<Vital> vitals = VitalsDAO.getVitalsByPatientId(patient.getUserId());

        // Prepare series for each vital sign
        XYChart.Series<String, Number> heartSeries = new XYChart.Series<>();
        heartSeries.setName("Heart Rate");

        XYChart.Series<String, Number> bpSeries    = new XYChart.Series<>();
        bpSeries.setName("Blood Pressure");

        XYChart.Series<String, Number> tempSeries  = new XYChart.Series<>();
        tempSeries.setName("Temperature");

        XYChart.Series<String, Number> oxySeries   = new XYChart.Series<>();
        oxySeries.setName("Oxygen Level");

        // Add data points to each series
        for (Vital v : vitals) {
            String timestamp = v.getTimestamp();
            if (timestamp != null && !timestamp.isBlank()) {
                heartSeries.getData().add(new XYChart.Data<>(timestamp, v.getHeartRate()));
                bpSeries.getData().add(new XYChart.Data<>(timestamp, v.getBloodPressure()));
                tempSeries.getData().add(new XYChart.Data<>(timestamp, v.getTemperature()));
                oxySeries.getData().add(new XYChart.Data<>(timestamp, v.getOxygenLevel()));
            } else {
                System.err.println("Skipping vital with missing timestamp: " + v);
            }
        }

        // Display all series on the chart
        lineChart.getData().addAll(heartSeries, bpSeries, tempSeries, oxySeries);
    }
}
