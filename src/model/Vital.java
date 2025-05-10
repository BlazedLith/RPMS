package model;

public class Vital {
    private double heartRate;
    private double bloodPressure;
    private double temperature;
    private double oxygenLevel;
    private String timestamp;

    public Vital(double heartRate, double bloodPressure, double temperature, double oxygenLevel) {
        this.setHeartRate(heartRate);
        this.setBloodPressure(bloodPressure);
        this.setTemperature(temperature);
        this.setOxygenLevel(oxygenLevel);
    }
    //Getters and Setters
    public double getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(double heartRate) {
        if (heartRate < 0) {
            throw new IllegalArgumentException("Heart rate cannot be negative");
        }
        this.heartRate = heartRate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(double bloodPressure) {
        if (bloodPressure < 0) {
            throw new IllegalArgumentException("Blood pressure cannot be negative");
        }
        this.bloodPressure = bloodPressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(double oxygenLevel) {
        if (oxygenLevel < 0) {
            throw new IllegalArgumentException("Oxygen level cannot be negative");
        }
        this.oxygenLevel = oxygenLevel;
    }
    //Function to parse the CSV file
    public static Vital parseCSVLine(String csvLine) {
        try {
            String[] tokens = csvLine.split(",");
            if(tokens.length < 4) {
                throw new IllegalArgumentException("CSV line does not contain enough data.");
            }
            double heartRate = Integer.parseInt(tokens[0].trim());
            double bloodPressure = Double.parseDouble(tokens[3].trim());
            double temperature = Double.parseDouble(tokens[2].trim());
            double oxygenLevel = Double.parseDouble(tokens[1].trim());

            return new Vital(heartRate, bloodPressure, temperature, oxygenLevel);
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing CSV line: " + csvLine);
            e.printStackTrace();
            return null;
        }
    }
}
