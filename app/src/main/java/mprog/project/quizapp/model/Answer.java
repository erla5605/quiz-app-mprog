package mprog.project.quizapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer extends QuizBaseEntity implements Parcelable {

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

    /* Implementation of the Parcelable interface */
    public Answer() {
    }

    protected Answer(Parcel in) {
        super(in);
        answerText = in.readString();
        isCorrectAnswer = in.readInt() != 0;
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(answerText);
        dest.writeInt(isCorrectAnswer ? 1 : 0);
    }


    /* To string method for Answer */
    @Override
    public String toString() {
        return "Answer{" +
                "answerText='" + answerText + '\'' +
                ", isCorrectAnswer=" + isCorrectAnswer +
                '}';
    }
}
