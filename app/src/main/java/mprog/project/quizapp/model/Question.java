package mprog.project.quizapp.model;

import java.util.Set;

class Question extends QuizBaseEntity {

    enum QuestionType {
        TEXT,
        PHOTO,
        VIDEO
    }

    private String questionText;
    private Set<Answer> answers;
    private QuestionType type;
    private String media;


    /* Getter and setters */

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}
