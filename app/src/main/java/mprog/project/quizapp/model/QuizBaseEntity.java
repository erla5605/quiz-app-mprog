package mprog.project.quizapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public abstract class QuizBaseEntity implements Parcelable {

    private UUID id = UUID.randomUUID();

    /* Getter and setters */

    public QuizBaseEntity() {
    }

    protected QuizBaseEntity(Parcel in) {
        this.id = (UUID) in.readSerializable();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(id);
    }
}
