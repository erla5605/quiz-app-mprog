package mprog.project.quizapp.storage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mprog.project.quizapp.model.Quiz;
import mprog.project.quizapp.storage.startup.QuizLoader;

public class QuizMapStorage {

    private static QuizMapStorage instance;

    private Map<UUID, Quiz> quizzes = new LinkedHashMap<>();

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

        quizzes.put(quiz.getId(), quiz);
    }

    // Delete quiz from map.
    public Quiz delete(UUID id){
        return quizzes.remove(id);
    }

    // Get quiz from map by id.
    public Quiz getQuiz(UUID id){
        return quizzes.get(id);
    }

    // Get all quizzes as a list.
    public List<Quiz> getQuizzes(){
        return new ArrayList<>(quizzes.values());
    }

}
