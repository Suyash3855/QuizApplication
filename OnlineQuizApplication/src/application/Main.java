package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import application.util.DatabaseUtil;

public class Main extends Application {
    private static Connection connection;

    @Override
    public void start(Stage primaryStage) {
        try {
            connection = DatabaseUtil.getConnection();
            Parent root = FXMLLoader.load(getClass().getResource("resources/application.views/login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Online Quiz Application");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Connection getConnection() {
        return connection;
    }
}
