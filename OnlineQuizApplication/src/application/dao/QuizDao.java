package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import application.models.Quiz;
import application.models.Question;

public class QuizDao {
    private Connection connection;

    public QuizDao(Connection connection) {
        this.connection = connection;
    }

    public boolean createQuiz(Quiz quiz) {
        String quizSql = "INSERT INTO quizzes (title) VALUES (?)";
        try (PreparedStatement quizStmt = connection.prepareStatement(quizSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            quizStmt.setString(1, quiz.getTitle());
            quizStmt.executeUpdate();
            ResultSet rs = quizStmt.getGeneratedKeys();
            if (rs.next()) {
                int quizId = rs.getInt(1);
                for (Question question : quiz.getQuestions()) {
                    if (!createQuestion(question, quizId)) {
                        return false;
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Quiz> getAllQuizzes() {
        String sql = "SELECT * FROM quizzes";
        List<Quiz> quizzes = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setId(rs.getInt("id"));
                quiz.setTitle(rs.getString("title"));
                quiz.setQuestions(getQuestionsByQuizId(quiz.getId()));
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    private boolean createQuestion(Question question, int quizId) {
        String questionSql = "INSERT INTO questions (quiz_id, question_text, correct_answer) VALUES (?, ?, ?)";
        try (PreparedStatement questionStmt = connection.prepareStatement(questionSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            questionStmt.setInt(1, quizId);
            questionStmt.setString(2, question.getQuestionText());
            questionStmt.setString(3, question.getCorrectAnswer());
            questionStmt.executeUpdate();
            ResultSet rs = questionStmt.getGeneratedKeys();
            if (rs.next()) {
                int questionId = rs.getInt(1);
                for (String option : question.getOptions()) {
                    if (!createOption(option, questionId)) {
                        return false;
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean createOption(String optionText, int questionId) {
        String optionSql = "INSERT INTO options (question_id, option_text) VALUES (?, ?)";
        try (PreparedStatement optionStmt = connection.prepareStatement(optionSql)) {
            optionStmt.setInt(1, questionId);
            optionStmt.setString(2, optionText);
            optionStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Question> getQuestionsByQuizId(int quizId) {
        String sql = "SELECT * FROM questions WHERE quiz_id = ?";
        List<Question> questions = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setId(rs.getInt("id"));
                question.setQuestionText(rs.getString("question_text"));
                question.setCorrectAnswer(rs.getString("correct_answer"));
                question.setOptions(getOptionsByQuestionId(question.getId()));
                questions.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    private List<String> getOptionsByQuestionId(int questionId) {
        String sql = "SELECT * FROM options WHERE question_id = ?";
        List<String> options = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                options.add(rs.getString("option_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return options;
    }
}
