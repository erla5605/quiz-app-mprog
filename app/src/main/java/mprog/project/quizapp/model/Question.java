package mprog.project.quizapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Question extends QuizBaseEntity implements Parcelable{


    public enum QuestionType {
        TEXT,
        PHOTO,
        VIDEO
    }

    private String questionText;
    private Set<Answer> answers;
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
        return new ArrayList<>(answers);
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

    protected Question(Parcel in) {
        questionText = in.readString();
        Answer[] answersArray = in.createTypedArray(Answer.CREATOR);
        answers = new HashSet<>(Arrays.asList(answersArray));
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
