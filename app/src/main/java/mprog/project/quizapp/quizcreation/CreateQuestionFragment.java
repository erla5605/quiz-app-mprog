package mprog.project.quizapp.quizcreation;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import mprog.project.quizapp.model.Answer;
import mprog.project.quizapp.model.Question;

public class CreateQuestionFragment extends Fragment
        implements SetCorrectAnswerDialogFragment.setCorrectAnswerDialogListener,
        CreateAnswerDialogFragment.CreateAnswerDialogFragmentListener,
        AddVideoDialogFragment.AddVideoDialogFragmentListener {

    // Listener interface to handle a new Question.
    interface CreateQuestionListener {

        void questionCreated(Question question);
    }

    private static final String QUESTION_KEY = "question_key";
    private static final String QUESTION_ARG = "question";
    private static final String SET_CORRECT_ANSWER_TAG = "set correct answer";
    private static final String CREATE_ANSWER_TAG = "create answer";
    private static final String ADD_VIDEO_TAG = "add video";

    private static final int SET_CORRECT_ANSWER_REQUEST_CODE = 200;
    private static final int CREATE_ANSWER_REQUEST_CODE = 201;
    private static final int ADD_VIDEO_REQUEST_CODE = 202;

    private EditText questionEditText;
    private Button addVideoButton;
    private TextView videoUrlTextView;
    private FloatingActionButton addAnswerButton;

    private RecyclerView answerRecyclerView;
    private AnswerAdapter answerAdapter;

    private Question question = new Question();
    private boolean updateQuestion;

    private CreateQuestionListener listener;

    // Creates and returns a CreateQuestionFragment instance with arguments for the question.
    public static CreateQuestionFragment newInstance(Question question) {
        Bundle args = new Bundle();
        args.putParcelable(QUESTION_ARG, question);

        CreateQuestionFragment createQuestionFragment = new CreateQuestionFragment();
        createQuestionFragment.setArguments(args);
        return createQuestionFragment;
    }

    /*  OnCreate,
        Gets and sets the question from either arguments or the savedInstanceState bundle
        Set has option menu.*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState != null) {
            question = savedInstanceState.getParcelable(QUESTION_KEY);
        } else {
            Bundle args = getArguments();
            if (args != null) {
                question = args.getParcelable(QUESTION_ARG);
                updateQuestion = true;
            }
        }

        setHasOptionsMenu(true);
    }

    /*  OnCreateView, creates the view sets up the edit text, text view and button.
        As well as the recycler view to display the different answers created.*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_question, container, false);

        questionEditText = v.findViewById(R.id.create_question_edit_text);

        addVideoButton = v.findViewById(R.id.add_video_button);
        addVideoButton.setOnClickListener(new View.OnClickListener() {
            // Start the dialog to enter a video url.
            @Override
            public void onClick(View v) {
                AddVideoDialogFragment dialogFragment = new AddVideoDialogFragment();
                dialogFragment.setTargetFragment(CreateQuestionFragment.this, ADD_VIDEO_REQUEST_CODE);
                dialogFragment.show(getFragmentManager(), ADD_VIDEO_TAG);

            }
        });
        videoUrlTextView = v.findViewById(R.id.video_url_text_view);

        answerRecyclerView = v.findViewById(R.id.answer_recycler_view);
        answerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        answerAdapter = new AnswerAdapter(question.getAnswers());
        answerRecyclerView.setAdapter(answerAdapter);

        addAnswerButton = v.findViewById(R.id.add_answer_floating_button);
        addAnswerButton.setOnClickListener(new View.OnClickListener() {
            // Starts the dialog to add an answer to the question.
            @Override
            public void onClick(View v) {
                CreateAnswerDialogFragment dialogFragment = new CreateAnswerDialogFragment();
                dialogFragment.setTargetFragment(CreateQuestionFragment.this, CREATE_ANSWER_REQUEST_CODE);
                dialogFragment.show(getFragmentManager(), CREATE_ANSWER_TAG);
            }
        });

        questionEditText.setText(question.getQuestionText());
        videoUrlTextView.setText(question.getVideo());

        return v;
    }

    // Creates the menu.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_create, menu);
    }

    /*  Handles if the user has selected an option in the menu.
        Calls on the listener to add the question to the quiz, if the question is not being updated.*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_quiz_item:
                if (isQuestionComplete()) {
                    question.setQuestionText(questionEditText.getText().toString());
                    if (question.getType() == null) {
                        question.setType(Question.QuestionType.TEXT);
                    }
                    getActivity().onBackPressed();
                    if (!updateQuestion) {
                        listener.questionCreated(question);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.incomplete_question_text, Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Checks if all the necessary fields have been set for the question to be created.
    private boolean isQuestionComplete() {
        return !questionEditText.getText().toString().isEmpty() && question.hasCorrectAnswer();
    }

    // Sets the correct answer in the question.
    @Override
    public void setCorrectAnswer(int answerPosition) {
        question.setPositionOfCorrectAnswer(answerPosition);
        answerAdapter.notifyDataSetChanged();
    }

    // Set the video url in the question.
    @Override
    public void addVideo(String videoUrl) {
        question.setVideo(videoUrl);
        question.setType(Question.QuestionType.VIDEO);
        videoUrlTextView.setText(videoUrl);
    }

    // Creates the answer and adds it to the question.
    @Override
    public void createAnswer(String answerText) {
        Answer newAnswer = new Answer();
        newAnswer.setAnswerText(answerText);
        question.addAnswer(newAnswer);
        answerAdapter.setAnswers(question.getAnswers());
        answerAdapter.notifyItemInserted(question.getAnswers().size() - 1);
    }

    // Saves the question in outState bundle, to handle rotation.
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(QUESTION_KEY, question);
    }

    // Sets the listener, to the activity that calls it.
    public void setCreateQuestionListener(CreateQuestionListener listener) {
        this.listener = listener;
    }

    // ViewHolder for the recycler view.
    private class AnswerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Answer answer;
        private ImageButton deleteButton;
        private EditText answerEditText;
        private ImageView correctAnswerImageView;

        // Constructor for the AnswerHolder. Sets up the edit text, button, and image view.
        public AnswerHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_answer_item, parent, false));

            deleteButton = itemView.findViewById(R.id.delete_answer_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    question.removeAnswer(answer);
                    answerAdapter.notifyDataSetChanged();
                }
            });

            answerEditText = itemView.findViewById(R.id.item_answer_edit_text);
            answerEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Not used
                }
                // Sets the text in the answer if the user changes it.
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    answer.setAnswerText(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Not used
                }
            });
            correctAnswerImageView = itemView.findViewById(R.id.item_correct_answer_image_view);

            itemView.setOnClickListener(this);
        }

        // Bind the answer holder to an answer, set its edit text and image view based on the answers attributes.
        public void bind(Answer answer) {
            this.answer = answer;
            answerEditText.setText(answer.getAnswerText());
            correctAnswerImageView.setImageDrawable(
                    getResources()
                            .getDrawable(
                                    answer.isCorrectAnswer() ?
                                            R.drawable.ic_action_checked :
                                            R.drawable.ic_action_unchecked));
        }

        // OnClick listener for the image view to create an dialog to set the to be the correct answer for the question.
        @Override
        public void onClick(View v) {
            SetCorrectAnswerDialogFragment dialog = SetCorrectAnswerDialogFragment.newInstance(getAdapterPosition());
            dialog.setTargetFragment(CreateQuestionFragment.this, SET_CORRECT_ANSWER_REQUEST_CODE);
            dialog.show(getFragmentManager(), SET_CORRECT_ANSWER_TAG);
        }
    }

    // Adapter for the recycler view.
    private class AnswerAdapter extends RecyclerView.Adapter<AnswerHolder> {

        private List<Answer> answers;

        // Creates the adapter with a list of answers.
        public AnswerAdapter(List<Answer> answers) {
            this.answers = answers;
        }

        // Creates answer holder.
        @NonNull
        @Override
        public AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new AnswerHolder(inflater, parent);
        }

        // Calls on the answer holder to bind an answer.
        @Override
        public void onBindViewHolder(@NonNull AnswerHolder holder, int position) {
            Answer question = answers.get(position);
            holder.bind(question);
        }

        // Gets the size of the list.
        @Override
        public int getItemCount() {
            return answers.size();
        }

        // Set the list of answers.
        public void setAnswers(List<Answer> answers) {
            this.answers = answers;
        }

    }
}
