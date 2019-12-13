package mprog.project.quizapp.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mprog.project.quizapp.model.Quiz;

public class QuizMapStorage {

    private static QuizMapStorage instance;

    private Map<Long, Quiz> quizzes = new LinkedHashMap<>();

    // Get instance of QuizMapStorage. If instance null creates a new.
    public static QuizMapStorage getInstance() {
        if(instance == null){
            instance = new QuizMapStorage();
        }
        return instance;
    }

    private QuizMapStorage(){
    }

    // Add quiz to map.
    public Quiz add(Quiz quiz){
        if(quiz == null)
            throw new RuntimeException("Quiz can not be null");

        if(quiz.getId() == null){
            Long id = quizzes.keySet().isEmpty() ? 1l : Collections.max(quizzes.keySet()) + 1l;
            quiz.setId(id);
        }

        return quizzes.put(quiz.getId(), quiz);
    }

    // Delete quiz from map.
    public Quiz delete(Quiz quiz){
        return quizzes.remove(quiz.getId());
    }

    // Get quiz from map by id.
    public Quiz getQuiz(Long id){
        return quizzes.get(id);
    }

    // Get all quizzes as a list.
    public List<Quiz> getQuizzes(){
        return new ArrayList<>(quizzes.values());
    }

}
