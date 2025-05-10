package model;

import controller.AdminDashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Administrator extends User{

    public Administrator(String userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    @Override
    public void displayDashboard(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDashboard.fxml"));
            Parent root = loader.load();

            AdminDashboardController controller = loader.getController();
            controller.setAdmin(this);

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Admin Dashboard");
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
