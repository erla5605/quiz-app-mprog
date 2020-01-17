package mprog.project.quizapp.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import mprog.project.quizapp.R;
import mprog.project.quizapp.api.QuizApi;
import mprog.project.quizapp.model.Question;
import mprog.project.quizapp.model.Quiz;
import mprog.project.quizapp.quizcomplete.CompletedQuizActivity;

public class QuizFragment extends Fragment implements CompleteQuizDialogFragment.CompleteQuizDialogListener,
        QuizApi.QuizApiResponseListener {

    private static final String QUIZ_ID_ARG = "quiz id";
    private static final String COMPLETE_QUIZ_TAG = "complete quiz";
    private static final int QUESTION_ANSWER_REQUEST_CODE = 200;
    private static final int COMPLETE_QUIZ_REQUEST_CODE = 201;
    private static final String QUESTION_ANSWERS_MAP = "question_answer_map";

    private TextView quizTitleTextView;
    private TextView quizDescriptionTextView;
    private Button completeQuizButton;

    private RecyclerView questionRecyclerView;
    private QuestionAdapter questionAdapter;

    private Quiz quiz;
    private HashMap<UUID, Integer> questionAnswers = new HashMap<>();

    // Creates a QuizFragment with the quiz id as argument.
    public static QuizFragment newInstance(UUID quizId) {
        Bundle args = new Bundle();
        args.putSerializable(QUIZ_ID_ARG, quizId);

        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // OnCreate, Gets the quiz id argument and the quiz.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID id = (UUID) getArguments().getSerializable(QUIZ_ID_ARG);

        new QuizApi(this, getActivity()).getQuiz(id);
    }

    // OnCreateView, creates the view, sets up text views, recycler view and button.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);

        if (savedInstanceState != null) {
            questionAnswers = (HashMap<UUID, Integer>) savedInstanceState.getSerializable(QUESTION_ANSWERS_MAP);
        }

        quizTitleTextView = v.findViewById(R.id.quiz_name_text_view);

        quizDescriptionTextView = v.findViewById(R.id.quiz_description_text_view);

        questionRecyclerView = v.findViewById(R.id.question_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        questionRecyclerView.setLayoutManager(linearLayoutManager);



        completeQuizButton = v.findViewById(R.id.complete_quiz_button);
        completeQuizButton.setOnClickListener(new View.OnClickListener() {
            // Starts dialog to complete the quiz.
            @Override
            public void onClick(View v) {
                CompleteQuizDialogFragment dialog = new CompleteQuizDialogFragment();
                dialog.setTargetFragment(QuizFragment.this, COMPLETE_QUIZ_REQUEST_CODE);
                dialog.show(getFragmentManager(), COMPLETE_QUIZ_TAG);
            }
        });

        return v;
    }


    // Sets up the texts of the text views.
    private void setUpView() {
        quizTitleTextView.setText(quiz.getName());
        quizDescriptionTextView.setText(quiz.getDescription());
        setUpAdapter();
    }

    // Sets up the adapter for the recycler view.
    private void setUpAdapter() {
        if(questionAdapter == null){
            questionAdapter = new QuestionAdapter(quiz.getQuestions());
            questionRecyclerView.setAdapter(questionAdapter);
        } else {
            questionAdapter.setQuestions(quiz.getQuestions());
            questionAdapter.notifyDataSetChanged();
        }
    }

    // Calculates the total score of the quiz and converts it to a percentage.
    private double getQuizScore() {
        int sum = 0;
        for (int i : questionAnswers.values()) {
            sum += i;
        }
        return (double) sum / quiz.getQuestions().size() * 100;
    }

    // Starts the complete CompletedQuizActivity if the user selects yes on the complete quiz dialog.
    @Override
    public void onYesButtonClicked() {
        double quizScorePercentage = getQuizScore();
        Intent intent = CompletedQuizActivity.newIntent(getActivity(), quiz.getName(), quizScorePercentage);
        startActivity(intent);
        getActivity().finish();
    }

    // Saves the answers, to handle rotation.
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(QUESTION_ANSWERS_MAP, questionAnswers);
    }

    // Handles the result from QuestionActivity and QuestionFragment.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QUESTION_ANSWER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            handleAnsweredQuestion(data);
        }
    }

    // Gets the result extras and update the answer map.
    private void handleAnsweredQuestion(Intent data) {
        Bundle extras = data.getExtras();
        boolean answeredCorrectly = extras.getBoolean(QuestionFragment.ANSWER_EXTRA);
        Question answeredQuestion = extras.getParcelable(QuestionFragment.QUESTION_EXTRA);
        questionAnswers.put(answeredQuestion.getId(), answeredCorrectly ? 1 : 0);
        questionAdapter.notifyDataSetChanged();
    }

    // Handles error responses on getting the quiz.
    @Override
    public void errorResponse(String error) {
        Toast.makeText(getActivity(), getString(R.string.error_load_quiz), Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    // Not used
    @Override
    public void quizListResponse(List<Quiz> quizzes) {

    }

    // Handles the response of getting the quiz.
    @Override
    public void quizResponse(Quiz quiz) {
        this.quiz = quiz;
        setUpView();
    }

    // not used
    @Override
    public void postResponse() {

    }

    // View holder for the recycler view.
    private class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Question question;
        private TextView questionText;
        private RadioButton questionRadioButton;

        // Constructor for the QuestionHolder, sets up the radio button, text view.
        public QuestionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_question_item, parent, false));

            questionText = itemView.findViewById(R.id.item_question_text);
            questionRadioButton = itemView.findViewById(R.id.item_question_radio_button);

            itemView.setOnClickListener(this);
        }

        // Binds the holder to a question, and sets the text for the text view and if radio button is checked.
        public void bind(Question question) {
            this.question = question;
            questionText.setText(this.question.getQuestionText());
            questionRadioButton.setChecked(questionAnswers.get(question.getId()) != null);
        }

        // Starts a new activity to answer the question.
        @Override
        public void onClick(View v) {
            Intent intent = QuestionActivity.newIntent(getActivity(), question);
            startActivityForResult(intent, QUESTION_ANSWER_REQUEST_CODE);
        }
    }

    // Adapter for the recycler view.
    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {

        private List<Question> questions;

        // Creates the adapter with a list of questions.
        public QuestionAdapter(List<Question> questions) {
            this.questions = questions;
        }

        // Creates a question holder.
        @NonNull
        @Override
        public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new QuestionHolder(inflater, parent);
        }

        // Calls on the holder to bind a question.
        @Override
        public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {
            Question question = questions.get(position);
            holder.bind(question);
        }

        // Returns the size of the questions list.
        @Override
        public int getItemCount() {
            return questions.size();
        }

        //Sets the questions
        public void setQuestions(List<Question> questions){
            this.questions = questions;
        }
    }
}
