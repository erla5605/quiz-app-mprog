package mprog.project.quizapp.quizcreation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import mprog.project.quizapp.model.Question;
import mprog.project.quizapp.model.Quiz;
import mprog.project.quizapp.storage.QuizMapStorage;

public class CreateQuizFragment extends Fragment implements CreateQuestionFragment.CreateQuestionListener {

    public static final String QUIZ_RESULT_EXTRA = "result quiz id";
    private static final String QUESTIONS_KEY = "questions";

    private EditText nameEditText;
    private EditText descriptionEditText;

    private RecyclerView questionRecyclerView;
    private QuestionAdapter questionAdapter;

    private FloatingActionButton addQuestionButton;

    private Quiz quiz = new Quiz();

    // OnCreate, set the menu. If the savedInstanceState is not null set the quiz questions.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            quiz.setQuestions(savedInstanceState.<Question>getParcelableArrayList(QUESTIONS_KEY));
        }

        setHasOptionsMenu(true);
    }

    // OnCreateView, creates the view, sets up the edit texts, the button, and the recycler view.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_quiz, container, false);

        nameEditText = v.findViewById(R.id.quiz_name_edit_text);
        descriptionEditText = v.findViewById(R.id.quiz_description_edit_text);

        questionRecyclerView = v.findViewById(R.id.new_question_recycler_view);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        questionAdapter = new QuestionAdapter(quiz.getQuestions());
        questionRecyclerView.setAdapter(questionAdapter);

        addQuestionButton = v.findViewById(R.id.add_question_floating_button);
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            // Creates the CreateQuestionFragment for a new question.
            @Override
            public void onClick(View v) {
                CreateQuestionFragment fragment = new CreateQuestionFragment();
                openCreateQuestion(fragment);
            }
        });

        return v;
    }

    // Starts the CreateQuestionFragment.
    private void openCreateQuestion(CreateQuestionFragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Creates the menu.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_create, menu);
    }

    /*  Handles if user has selected a menu item
        Checks if the quiz is completed, if so creates the quiz and finishes the activity.*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_quiz_item:
                if (isQuizComplete()) {
                    createQuiz();
                    Intent intent = new Intent();
                    intent.putExtra(QUIZ_RESULT_EXTRA, true);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), R.string.incomplete_quiz_text, Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Creates the quiz by setting it's name and description and saving it to storage.
    private void createQuiz() {
        quiz.setName(nameEditText.getText().toString());
        quiz.setDescription(descriptionEditText.getText().toString());
        QuizMapStorage.getInstance().add(quiz);
    }

    // Checks if everything necessary for the quiz has been set for the quiz to be complete.
    private boolean isQuizComplete() {
        return !nameEditText.getText().toString().isEmpty() &&
                !descriptionEditText.getText().toString().isEmpty() &&
                isQuestionsComplete();
    }

    // Checks if the questions are complete.
    private boolean isQuestionsComplete() {
        if (quiz.getQuestions().isEmpty()) {
            return false;
        }

        for (Question q : quiz.getQuestions()) {
            if (!q.hasCorrectAnswer()) {
                return false;
            }
        }
        return true;
    }

    // Adds the question to the quiz, and notifies the adapter.
    @Override
    public void questionCreated(Question question) {
        quiz.addQuestion(question);
        questionAdapter.notifyDataSetChanged();
    }

    // Saves the list of questions, to handle rotation.
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(QUESTIONS_KEY, quiz.getQuestions());

    }

    // View holder for the recycler view.
    private class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Question question;
        private ImageButton deleteButton;
        private TextView questionText;
        private ImageView questionTypeImageView;

        // Constructor for the QuestionHolder, sets up the button, text view and image view.
        public QuestionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_new_question_item, parent, false));

            deleteButton = itemView.findViewById(R.id.delete_question_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                // removes the question and notifies the adapter.
                @Override
                public void onClick(View view) {
                    quiz.removeQuestion(question);
                    questionAdapter.notifyDataSetChanged();
                }
            });

            questionText = itemView.findViewById(R.id.new_question_text_view);
            questionTypeImageView = itemView.findViewById(R.id.question_type_image_view);

            itemView.setOnClickListener(this);
        }

        // Binds the holder to a question, and sets the text and image drawable.
        public void bind(Question question) {
            this.question = question;
            questionText.setText(question.getQuestionText());

            questionTypeImageView.setImageDrawable(getDrawable(question));
        }
        // Gets the image drawable based on question type.
        private Drawable getDrawable(Question question) {
            return question.getType() == Question.QuestionType.VIDEO ?
                    getResources().getDrawable(R.drawable.ic_action_video) : getResources().getDrawable(R.drawable.ic_action_text);
        }

        // // Creates the CreateQuestionFragment to update the question.
        @Override
        public void onClick(View v) {
            CreateQuestionFragment fragment = CreateQuestionFragment.newInstance(question);
            openCreateQuestion(fragment);
        }
    }

    // Adapter for the recycler view.
    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {

        private List<Question> questions;

        // Creates the adapter with a list of questions.
        public QuestionAdapter(List<Question> questions) {
            this.questions = questions;
        }

        // Creates question holder.
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

        // Returns the question list.
        @Override
        public int getItemCount() {
            return questions.size();
        }
    }
}
