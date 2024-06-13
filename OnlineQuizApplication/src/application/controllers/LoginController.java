package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import application.models.User;
import application.dao.UserDao;
import application.util.SecurityUtil;
import application.Main;

import java.sql.Connection;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;

    private UserDao userDao;

    public LoginController() {
        Connection connection = Main.getConnection();
        userDao = new UserDao(connection);
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = userDao.getUserByUsername(username);
        if (user != null && user.getPasswordHash().equals(SecurityUtil.hashPassword(password))) {
            messageLabel.setText("Login successful!");
            // Navigate to quiz screen
            navigateToQuizScreen();
        } else {
            messageLabel.setText("Invalid username or password");
        }
    }

    @FXML
    public void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (userDao.createUser(username, SecurityUtil.hashPassword(password))) {
            messageLabel.setText("Registration successful!");
        } else {
            messageLabel.setText("Registration failed");
        }
    }

    private void navigateToQuizScreen() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/application/views/quiz.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
