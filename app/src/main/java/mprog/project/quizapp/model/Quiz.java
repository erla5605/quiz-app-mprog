package mprog.project.quizapp.model;

import java.util.HashSet;
import java.util.Set;

public class Quiz extends QuizBaseEntity {

    private String name;
    private String description;
    private Set<Question> questions = new HashSet<>();

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

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }
}
