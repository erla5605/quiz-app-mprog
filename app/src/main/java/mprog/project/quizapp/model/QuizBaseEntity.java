package mprog.project.quizapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;
import java.util.UUID;

public abstract class QuizBaseEntity implements Parcelable {

    private UUID id = UUID.randomUUID();


    /* Getter and setters */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    /* Implementation of the Parcelable interface */
    public QuizBaseEntity() {
    }

    protected QuizBaseEntity(Parcel in) {
        this.id = (UUID) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(id);
    }

    // Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuizBaseEntity)) return false;
        QuizBaseEntity that = (QuizBaseEntity) o;
        return Objects.equals(id, that.id);
    }

    // Hashcode
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
