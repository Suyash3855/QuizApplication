package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import application.dao.QuizDao;
import application.models.Quiz;
import application.Main;

import java.sql.Connection;
import java.util.List;

public class QuizController {
    @FXML
    private ListView<String> quizListView;

    private QuizDao quizDao;

    public QuizController() {
        Connection connection = Main.getConnection();
        quizDao = new QuizDao(connection);
    }

    @FXML
    public void initialize() {
        List<Quiz> quizzes = quizDao.getAllQuizzes();
        for (Quiz quiz : quizzes) {
            quizListView.getItems().add(quiz.getTitle());
        }
    }

    @FXML
    public void handleStartQuiz() {
        String selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
        if (selectedQuiz != null) {
            // Navigate to quiz taking screen
            navigateToQuizTakingScreen(selectedQuiz);
        }
    }

    private void navigateToQuizTakingScreen(String quizTitle) {
        // Implementation to navigate to quiz taking screen with selected quiz
    }
}
