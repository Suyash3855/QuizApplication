package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import application.dao.QuizDao;
import application.models.Quiz;
import application.Main;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    @FXML
    private TextField quizTitleField;
    @FXML
    private ListView<String> quizListView;

    private QuizDao quizDao;

    public AdminController() {
        Connection connection = Main.getConnection();
        quizDao = new QuizDao(connection);
    }

    @FXML
    public void initialize() {
        loadQuizzes();
    }

    private void loadQuizzes() {
        quizListView.getItems().clear();
        List<Quiz> quizzes = quizDao.getAllQuizzes();
        for (Quiz quiz : quizzes) {
            quizListView.getItems().add(quiz.getTitle());
        }
    }

    @FXML
    public void handleAddQuiz() {
        String quizTitle = quizTitleField.getText();
        if (!quizTitle.isEmpty()) {
            Quiz quiz = new Quiz();
            quiz.setTitle(quizTitle);
            quiz.setQuestions(new ArrayList<>());
            if (quizDao.createQuiz(quiz)) {
                loadQuizzes();
            }
        }
    }

    @FXML
    public void handleDeleteQuiz() {
        String selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
        if (selectedQuiz != null) {
            // Implement deletion of selected quiz
        }
    }
}
