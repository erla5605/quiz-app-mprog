package mprog.project.quizapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question extends QuizBaseEntity implements Parcelable{


    public enum QuestionType {
        TEXT,
        PHOTO,
        VIDEO
    }

    private String questionText;
    private List<Answer> answers = new ArrayList<>();
    private int positionOfCorrectAnswer = -1;
    private QuestionType type;
    private String media;

    public Question() {
    }

    /* Getter and setters */

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer){
        answers.add(answer);
    }

    public int getPositionOfCorrectAnswer() {
        return positionOfCorrectAnswer;
    }

    public void setPositionOfCorrectAnswer(int positionOfCorrectAnswer) {
        if(positionOfCorrectAnswer > answers.size())
            throw new IllegalArgumentException("Position of Correct Answer out of bounds");

        if(this.positionOfCorrectAnswer != -1){
            answers.get(this.positionOfCorrectAnswer).setCorrectAnswer(false);
        }
        this.positionOfCorrectAnswer = positionOfCorrectAnswer;
        answers.get(this.positionOfCorrectAnswer).setCorrectAnswer(true);
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

    protected Question(Parcel in) {
        questionText = in.readString();
        Answer[] answersArray = in.createTypedArray(Answer.CREATOR);
        answers = new ArrayList<>(Arrays.asList(answersArray));
        type = QuestionType.values()[in.readInt()];
        media = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionText);
        dest.writeTypedArray(answers.toArray(new Answer[answers.size()]), flags);
        dest.writeInt(type.ordinal());
        dest.writeString(media);
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionText='" + questionText + '\'' +
                ", answers=" + answers +
                ", type=" + type +
                ", media='" + media + '\'' +
                '}';
    }
}
