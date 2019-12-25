package mprog.project.quizapp.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mprog.project.quizapp.model.Quiz;
import mprog.project.quizapp.storage.startup.QuizLoader;

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
        for(Quiz quiz : QuizLoader.getQuizzes()){
            add(quiz);
        }
    }

    // Add quiz to map.
    public void add(Quiz quiz){
        if(quiz == null)
            throw new RuntimeException("Quiz can not be null");

        if(quiz.getId() == null){
            Long id = quizzes.keySet().isEmpty() ? 1L : Collections.max(quizzes.keySet()) + 1L;
            quiz.setId(id);
        }

        quizzes.put(quiz.getId(), quiz);
    }

    // Delete quiz from map.
    public Quiz delete(Long id){
        Quiz deletedQuiz = quizzes.remove(id);

        return deletedQuiz;
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
