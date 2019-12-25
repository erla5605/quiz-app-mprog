package mprog.project.quizapp.storage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import mprog.project.quizapp.model.Question;

public class QuestionMapStorage {

    private static QuestionMapStorage instance;

    private Map<Long, Question> questions = new HashMap<>();

    public static QuestionMapStorage getInstance() {
        if (instance == null) {
            instance = new QuestionMapStorage();
        }
        return instance;
    }

    // Add Question to map.
    protected void add(Question question) {
        if (question == null)
            throw new RuntimeException("Quiz can not be null");

        Long id = questions.keySet().isEmpty() ? 1L : Collections.max(questions.keySet()) + 1L;
        question.setId(id);

        questions.put(question.getId(), question);
    }

    // Delete Question from map.
    protected void delete(Long id) {
        questions.remove(id);
    }

    // Get Question from map by id.
    public Question getQuestion(Long id) {
        return questions.get(id);
    }

}
