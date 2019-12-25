package mprog.project.quizapp.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz extends QuizBaseEntity {

    private String name;
    private String description;
    private List<Question> questions = new ArrayList<>();

    /* Getter and setters */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question){
        questions.add(question);
    }
}
