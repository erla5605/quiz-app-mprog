package mprog.project.quizapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer implements Parcelable {

    private String answerText;
    private boolean isCorrectAnswer;

    public Answer() {
    }

    protected Answer(Parcel in) {
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
        dest.writeString(answerText);
        dest.writeInt(isCorrectAnswer ? 1 : 0);
    }

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

    @Override
    public String toString() {
        return "Answer{" +
                "answerText='" + answerText + '\'' +
                ", isCorrectAnswer=" + isCorrectAnswer +
                '}';
    }
}
