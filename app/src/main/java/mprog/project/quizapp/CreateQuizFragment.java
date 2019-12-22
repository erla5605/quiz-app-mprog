package mprog.project.quizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CreateQuizFragment extends Fragment {

    private EditText nameEditText;
    private EditText descriptionEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_quiz, container, false);

        nameEditText = v.findViewById(R.id.quiz_name_edit_text);
        descriptionEditText = v.findViewById(R.id.quiz_description_edit_text);


        return v;
    }
}
