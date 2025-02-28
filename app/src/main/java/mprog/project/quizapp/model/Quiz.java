package mprog.project.quizapp.model;

import android.os.Parcel;

import java.util.ArrayList;

public class Quiz extends QuizBaseEntity {

    private String name;
    private String description;
    private ArrayList<Question> questions = new ArrayList<>();

    public Quiz() {
    }

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

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    // Add question to quiz
    public void addQuestion(Question newQuestion) {
        if (newQuestion == null) {
            return;
        }

        questions.add(newQuestion);
    }

    // Remove question from quiz
    public void removeQuestion(Question removeQuestion) {
        questions.remove(removeQuestion);

    }

    /* Implementation of the Parcelable interface */

    protected Quiz(Parcel in) {
        super(in);
        name = in.readString();
        description = in.readString();
        questions = in.createTypedArrayList(Question.CREATOR);
    }
    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeTypedList(questions);
    }

    /* To string method for Quiz */
    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + getId() +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", questions=" + questions +
                '}';
    }
}
