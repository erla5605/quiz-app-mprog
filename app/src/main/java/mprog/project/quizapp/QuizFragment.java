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
import java.util.Map;

import mprog.project.quizapp.model.Question;
import mprog.project.quizapp.model.Quiz;
import mprog.project.quizapp.storage.QuizMapStorage;

public class QuizFragment extends Fragment {

    private static final String TAG = "QuizFragment";

    private static final int QUESTION_ANSWER_REQUEST_CODE = 200;

    private TextView quizDescriptionTextView;
    private RecyclerView questionRecyclerView;
    private Button completeQuizButton;

    private QuestionAdapter questionAdapter;

    private Quiz quiz;

    private Map<Long, Integer> questionAnswers = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quiz = QuizMapStorage.getInstance().getQuizzes().get(0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);

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

                double quizScorePercentage = (double) getQuizScore() / quiz.getQuestions().size();
            }
        });

        return v;
    }

    private int getQuizScore() {
        int sum = 0;
        for (int i : questionAnswers.values()) {
            sum += i;
        }
        return sum;
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
            Intent intent = QuestionActivity.newIntent(getActivity(), question.getId());
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
        if (requestCode == QUESTION_ANSWER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            handledAnsweredQuestion(data);
        }
    }

    private void handledAnsweredQuestion(Intent data) {
        Bundle extras = data.getExtras();
        boolean answeredCorrectly = extras.getBoolean(QuestionFragment.ANSWER_EXTRA);
        long answeredQuestionId = extras.getLong(QuestionFragment.QUESTION_EXTRA);
        questionAnswers.put(answeredQuestionId, answeredCorrectly ? 1 : 0);
        questionAdapter.notifyDataSetChanged();
    }
}
