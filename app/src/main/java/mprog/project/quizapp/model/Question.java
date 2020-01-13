package mprog.project.quizapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Question extends QuizBaseEntity implements Parcelable {

    public enum QuestionType {
        TEXT,
        VIDEO
    }

    private String questionText;
    private List<Answer> answers = new ArrayList<>();
    private Answer correctAnswer;
    private QuestionType type;
    private String video;

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

    public void addAnswer(Answer newAnswer) {
        if (newAnswer == null) {
            return;
        }

        answers.add(newAnswer);
    }

    public void removeAnswer(Answer removeAnswer) {
        if (answers.remove(removeAnswer) && correctAnswer != null && correctAnswer.equals(removeAnswer)) {
            correctAnswer = null;
        }
    }

    public void setPositionOfCorrectAnswer(int positionOfCorrectAnswer) {
        if (positionOfCorrectAnswer > answers.size())
            throw new IllegalArgumentException("Position of Correct Answer out of bounds");

        if (this.correctAnswer != null) {
            this.correctAnswer.setCorrectAnswer(false);
        }
        this.correctAnswer = answers.get(positionOfCorrectAnswer);
        this.correctAnswer.setCorrectAnswer(true);
    }

    public boolean hasCorrectAnswer() {
        return correctAnswer != null;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    protected Question(Parcel in) {
        super(in);
        questionText = in.readString();
        answers = in.createTypedArrayList(Answer.CREATOR);
        correctAnswer = in.readParcelable(Answer.class.getClassLoader());
        type = QuestionType.values()[in.readInt()];
        video = in.readString();
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
        super.writeToParcel(dest, flags);
        dest.writeString(questionText);
        dest.writeTypedList(answers);
        dest.writeParcelable(correctAnswer, flags);
        dest.writeInt(type.ordinal());
        dest.writeString(video);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + getId() +
                "questionText='" + questionText + '\'' +
                ", answers=" + answers +
                ", type=" + type +
                ", video='" + video + '\'' +
                '}';
    }
}
