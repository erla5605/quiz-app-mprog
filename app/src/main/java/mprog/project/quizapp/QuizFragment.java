package mprog.project.quizapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import mprog.project.quizapp.model.Question;
import mprog.project.quizapp.model.Quiz;
import mprog.project.quizapp.quizcomplete.CompleteQuizDialogFragment;
import mprog.project.quizapp.quizcomplete.CompletedQuizActivity;
import mprog.project.quizapp.storage.QuizMapStorage;

public class QuizFragment extends Fragment implements CompleteQuizDialogFragment.CompleteQuizDialogListener {

    private static final String QUIZ_ID_ARG = "quiz id";

    private static final String COMPLETE_QUIZ_TAG = "complete quiz";
    private static final int QUESTION_ANSWER_REQUEST_CODE = 200;
    private static final int COMPLETE_QUIZ_REQUEST_CODE = 201;

    private static final String QUESTION_ANSWERS_MAP= "question_answer_map";

    private RecyclerView questionRecyclerView;
    private TextView quizTitleTextView;
    private TextView quizDescriptionTextView;
    private Button completeQuizButton;

    private QuestionAdapter questionAdapter;

    private Quiz quiz;

    private HashMap<UUID, Integer> questionAnswers = new HashMap<>();

    public static QuizFragment newInstance(UUID quizId){
        Bundle args = new Bundle();
        args.putSerializable(QUIZ_ID_ARG, quizId);

        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID id = (UUID) getArguments().getSerializable(QUIZ_ID_ARG);
        quiz = QuizMapStorage.getInstance().getQuiz(id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);

        if(savedInstanceState != null){
            questionAnswers = (HashMap<UUID, Integer>) savedInstanceState.getSerializable(QUESTION_ANSWERS_MAP);
        }

        quizTitleTextView = v.findViewById(R.id.quiz_name_text_view);
        quizTitleTextView.setText(quiz.getName());

        quizDescriptionTextView = v.findViewById(R.id.quiz_description_text_view);
        quizDescriptionTextView.setText(quiz.getDescription());

        questionRecyclerView = v.findViewById(R.id.question_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        questionRecyclerView.setLayoutManager(linearLayoutManager);

        questionAdapter = new QuestionAdapter(quiz.getQuestions());
        questionRecyclerView.setAdapter(questionAdapter);

        completeQuizButton = v.findViewById(R.id.complete_quiz_button);
        completeQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompleteQuizDialogFragment dialog = new CompleteQuizDialogFragment();
                dialog.setTargetFragment(QuizFragment.this,COMPLETE_QUIZ_REQUEST_CODE);
                dialog.show(getFragmentManager(), COMPLETE_QUIZ_TAG);
            }
        });

        return v;
    }

    private double getQuizScore() {
        int sum = 0;
        for (int i : questionAnswers.values()) {
            sum += i;
        }
        return (double)sum / quiz.getQuestions().size() * 100;
    }

    @Override
    public void onYesButtonClicked() {
        double quizScorePercentage = getQuizScore();
        Intent intent = CompletedQuizActivity.newIntent(getActivity(), quiz.getId(), quizScorePercentage);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(QUESTION_ANSWERS_MAP, questionAnswers);
    }

    private class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Question question;
        private TextView questionText;
        private RadioButton questionRadioButton;

        public QuestionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_question_item, parent, false));

            questionText = itemView.findViewById(R.id.item_question_text);
            questionRadioButton = itemView.findViewById(R.id.item_question_radio_button);

            itemView.setOnClickListener(this);
        }

        public void bind(Question question) {
            this.question = question;
            questionText.setText(this.question.getQuestionText());
            questionRadioButton.setChecked(questionAnswers.get(question.getId()) != null);
        }

        @Override
        public void onClick(View v) {
            Intent intent = QuestionActivity.newIntent(getActivity(), question);
            startActivityForResult(intent, QUESTION_ANSWER_REQUEST_CODE);
        }
    }

    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {

        private List<Question> questions;

        public QuestionAdapter(List<Question> questions) {
            this.questions = questions;
        }

        @NonNull
        @Override
        public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new QuestionHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {
            Question question = questions.get(position);
            holder.bind(question);
        }

        @Override
        public int getItemCount() {
            return questions.size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QUESTION_ANSWER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            handleAnsweredQuestion(data);
        }
    }

    private void handleAnsweredQuestion(Intent data) {
        Bundle extras = data.getExtras();
        boolean answeredCorrectly = extras.getBoolean(QuestionFragment.ANSWER_EXTRA);
        Question answeredQuestion = extras.getParcelable(QuestionFragment.QUESTION_EXTRA);
        questionAnswers.put(answeredQuestion.getId(), answeredCorrectly ? 1 : 0);
        questionAdapter.notifyDataSetChanged();
    }
}
