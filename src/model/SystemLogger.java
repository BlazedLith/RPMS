package model;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemLogger {

    private static final String LOG_FILE = "logs/system.log";
//Writes the required action in Log file
    public static void log(String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            out.println("[" + timestamp + "] " + message);
        } catch (Exception e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }
}
