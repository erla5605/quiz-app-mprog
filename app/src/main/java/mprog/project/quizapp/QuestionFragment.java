package mprog.project.quizapp;

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

    public static final String QUESTION_ID_ARG = "quiz_id";

    private Question question;
    private List<Answer> answers;
    private List<Integer> radioButtonIds = new ArrayList<>();

    private TextView questionTextView;
    private RadioGroup answersRadioGroup;
    private Button answerQuestionButton;

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
        answers = question.getAnswers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, container, false);

        questionTextView = v.findViewById(R.id.question_text_view);
        questionTextView.setText(question.getQuestionText());

        answersRadioGroup = v.findViewById(R.id.answers_radio_group);

        for(Answer answer : answers) {
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
                int id = answersRadioGroup.getCheckedRadioButtonId();
                int index = radioButtonIds.indexOf(id);

                if(answers.get(index).isCorrectAnswer()){
                    Toast.makeText(getContext(), "CORRECT", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "INCORRECT", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }
}
