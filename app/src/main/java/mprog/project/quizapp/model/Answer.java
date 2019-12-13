package mprog.project.quizapp.model;

public class Answer extends QuizBaseEntity {

    private String answerText;
    private boolean isCorrectAnswer;


    /* Getter and setters */

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }
}
