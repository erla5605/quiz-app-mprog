package mprog.project.quizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import mprog.project.quizapp.model.Question;
import mprog.project.quizapp.storage.QuestionMapStorage;

public class QuestionFragment extends Fragment {

    public static final String QUESTION_ID_ARG = "quiz_id";

    private Question question;

    private TextView questionTextView;

    public static QuestionFragment newQuestionFragment(Long quizId){
        QuestionFragment questionFragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putLong(QUESTION_ID_ARG, quizId);
        questionFragment.setArguments(args);
        return questionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Long questionId = getArguments().getLong(QUESTION_ID_ARG);
        question = QuestionMapStorage.getInstance().getQuestion(questionId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, container, false);

        questionTextView = v.findViewById(R.id.question_text_view);
        questionTextView.setText(question.getQuestionText());

        return v;
    }
}
