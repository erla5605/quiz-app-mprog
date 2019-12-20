package mprog.project.quizapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import mprog.project.quizapp.model.Answer;
import mprog.project.quizapp.model.Question;
import mprog.project.quizapp.storage.QuestionMapStorage;

public class QuestionFragment extends Fragment {

    public static final String ANSWER_EXTRA = "answer_boolean";
    public static final String QUESTION_EXTRA = "answered_question_id";
    public static final String QUESTION_ID_ARG = "question_id";

    private Question question;
    private List<Answer> answers;
    private List<Integer> radioButtonIds = new ArrayList<>();

    private TextView questionTextView;
    private RadioGroup answersRadioGroup;
    private Button answerQuestionButton;

    public static QuestionFragment newInstance(Long quizId) {
        Bundle args = new Bundle();
        args.putLong(QUESTION_ID_ARG, quizId);

        QuestionFragment questionFragment = new QuestionFragment();
        questionFragment.setArguments(args);
        return questionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Long questionId = getArguments().getLong(QUESTION_ID_ARG);
        question = QuestionMapStorage.getInstance().getQuestion(questionId);
        answers = question.getAnswers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, container, false);

        questionTextView = v.findViewById(R.id.question_text_view);
        questionTextView.setText(question.getQuestionText());

        answersRadioGroup = v.findViewById(R.id.answers_radio_group);

        for (Answer answer : answers) {
            RadioButton rButton = new RadioButton(v.getContext());
            int radioButtonId = v.generateViewId();
            rButton.setId(radioButtonId);
            radioButtonIds.add(radioButtonId);
            rButton.setText(answer.getAnswerText());
            answersRadioGroup.addView(rButton);
        }

        answerQuestionButton = v.findViewById(R.id.answer_question_button);
        answerQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = answersRadioGroup.getCheckedRadioButtonId(); // Returns -1 if no button checked.
                if (id != -1) {
                    setQuestionResult(id);
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "NO ANSWER", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void setQuestionResult(int id) {
        int index = radioButtonIds.indexOf(id);
        boolean answeredCorrectly =  answers.get(index).isCorrectAnswer();
        Bundle extras = new Bundle();
        extras.putBoolean(ANSWER_EXTRA, answeredCorrectly);
        extras.putLong(QUESTION_EXTRA, question.getId());
        Intent intent = new Intent().putExtras(extras);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }
}
