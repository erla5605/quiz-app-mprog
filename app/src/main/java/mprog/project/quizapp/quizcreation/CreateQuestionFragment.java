package mprog.project.quizapp.quizcreation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import mprog.project.quizapp.R;
import mprog.project.quizapp.model.Answer;
import mprog.project.quizapp.model.Question;

public class CreateQuestionFragment extends Fragment
        implements SetCorrectAnswerDialogFragment.setCorrectAnswerDialogListener,
        CreateAnswerDialogFragment.CreateAnswerDialogFragmentListener {

    interface CreateQuestionListener{
        void questionCreated(Question question);
    }

    private static final String TAG = "CreateQuestionFragment";

    private static final int SET_CORRECT_ANSWER_REQUEST_CODE = 200;
    private static final int CREATE_ANSWER_REQUEST_CODE = 201;
    private static final String SET_CORRECT_ANSWER_TAG = "set correct answer";
    private static final String CREATE_ANSWER_TAG = "create answer";

    private EditText questionEditText;

    private RecyclerView answerRecyclerView;
    private AnswerAdapter answerAdapter;

    private FloatingActionButton addAnswerButton;

    private Question question = new Question();

    private CreateQuestionListener listener;

    public CreateQuestionFragment(CreateQuestionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_question, container, false);

        questionEditText = v.findViewById(R.id.create_question_edit_text);

        answerRecyclerView = v.findViewById(R.id.answer_recycler_view);
        answerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        answerAdapter = new AnswerAdapter(question.getAnswers());
        answerRecyclerView.setAdapter(answerAdapter);

        addAnswerButton = v.findViewById(R.id.add_answer_floating_button);
        addAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAnswerDialogFragment dialogFragment = new CreateAnswerDialogFragment();
                dialogFragment.setTargetFragment(CreateQuestionFragment.this, CREATE_ANSWER_REQUEST_CODE);
                dialogFragment.show(getFragmentManager(), CREATE_ANSWER_TAG);
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_create, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.done_quiz_item:
                if(isQuestionComplete()){
                    question.setQuestionText(questionEditText.getText().toString());
                    listener.questionCreated(question);
                    getActivity().onBackPressed();

                } else {
                    Toast.makeText(getActivity(), R.string.incomplete_question_text, Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }




    }

    private boolean isQuestionComplete() {
        return !questionEditText.getText().toString().isEmpty() && question.hasCorrectAnswer();
    }

    @Override
    public void setCorrectAnswer(int answerPosition) {
        question.setPositionOfCorrectAnswer(answerPosition);
        answerAdapter.notifyDataSetChanged();
    }

    @Override
    public void createAnswer(String answerText) {
        Answer newAnswer = new Answer();
        newAnswer.setAnswerText(answerText);
        question.addAnswer(newAnswer);
        answerAdapter.setAnswers(question.getAnswers());
        answerAdapter.notifyItemInserted(question.getAnswers().size() - 1);
    }

    private class AnswerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Answer answer;
        private TextView answerTextView;
        private RadioButton correctAnswerRadioButton;

        public AnswerHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_answer_item, parent, false));

            answerTextView = itemView.findViewById(R.id.item_answer_text_view);
            correctAnswerRadioButton = itemView.findViewById(R.id.item_correct_answer_radio_button);

            itemView.setOnClickListener(this);
        }

        public void bind(Answer answer) {
            this.answer = answer;
            answerTextView.setText(answer.getAnswerText());
            correctAnswerRadioButton.setChecked(answer.isCorrectAnswer());
        }

        @Override
        public void onClick(View v) {
            SetCorrectAnswerDialogFragment dialog = SetCorrectAnswerDialogFragment.newInstance(getAdapterPosition());
            dialog.setTargetFragment(CreateQuestionFragment.this, SET_CORRECT_ANSWER_REQUEST_CODE);
            dialog.show(getFragmentManager(), SET_CORRECT_ANSWER_TAG);
        }


    }

    private class AnswerAdapter extends RecyclerView.Adapter<AnswerHolder> {

        private List<Answer> answers;

        public AnswerAdapter(List<Answer> answers) {
            this.answers = answers;
        }

        @NonNull
        @Override
        public AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new AnswerHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull AnswerHolder holder, int position) {
            Answer question = answers.get(position);
            holder.bind(question);
        }

        @Override
        public int getItemCount() {
            return answers.size();
        }


        public void setAnswers(List<Answer> answers) {
            this.answers = answers;
        }
    }


}
