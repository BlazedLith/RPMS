package controller;

import db.UserDAO;
import db.VitalsDAO;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import model.EmailService;
import model.Patient;
import model.UIHelper;
import model.Vital;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class VitalsUploadController {

    private Patient patient;  // Current logged-in patient

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @FXML
    public void handleBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Vitals CSV");
        File file = fileChooser.showOpenDialog(null);

        if (file == null || patient == null) {
            UIHelper.showError("No file selected or patient not set.");
            return;
        }

        List<Vital> uploadedVitals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Vital vital = Vital.parseCSVLine(line);
                if (vital == null) continue;

                int nextId = VitalsDAO.getMaxVitalIdGlobal() + 1;
                VitalsDAO.addVital(vital, patient.getUserId(), nextId);
                uploadedVitals.add(vital);

                // Alert doctor if vitals are outside normal range
                if (isAbnormal(vital)) {
                    String doctorEmail = UserDAO.getDoctorEmailByPatientId(patient.getUserId());
                    String subject = "🚨 Abnormal Vitals Alert: " + patient.getName();
                    String body = String.format(
                            "Patient: %s%nHeart Rate: %.1f%nOxygen Level: %.1f%nTemperature: %.1f%nBlood Pressure: %.1f",
                            patient.getName(),
                            vital.getHeartRate(),
                            vital.getOxygenLevel(),
                            vital.getTemperature(),
                            vital.getBloodPressure()
                    );
                    EmailService.sendEmail(doctorEmail, subject, body);
                }
            }
            UIHelper.showSuccess("Uploaded " + uploadedVitals.size() + " vital(s) for " + patient.getName() + ".");
        } catch (Exception e) {
            UIHelper.showError("Failed to upload vitals: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Checks if any vital sign is outside safe thresholds
    private boolean isAbnormal(Vital v) {
        return v.getHeartRate() < 60 || v.getHeartRate() > 100
                || v.getOxygenLevel() < 90
                || v.getTemperature() > 38.0;
    }
}

