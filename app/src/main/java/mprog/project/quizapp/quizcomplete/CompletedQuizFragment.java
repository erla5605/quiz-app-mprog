package mprog.project.quizapp.quizcomplete;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import mprog.project.quizapp.R;
import mprog.project.quizapp.model.Quiz;
import mprog.project.quizapp.storage.QuizMapStorage;


public class CompletedQuizFragment extends Fragment implements ShareCompletedQuizDialogFragment.ShareCompletedQuizDialogListener {

    private static final String TAG = "CompletedQuizFragment";
    private static final String QUIZ_ID_ARG = "quiz_id";
    private static final String SCORE_ARG = "score";
    private static final String SHARE_QUIZ_TAG = "share quiz";
    private static final int SHARE_QUIZ_REQUEST_CODE = 200;

    private TextView quizNameTextView;
    private TextView quizDateTextView;
    private TextView quizScoreTextView;

    private Button shareButton;

    private Quiz quiz;

    private double score;

    public static CompletedQuizFragment newInstance(Long quizId, double score) {
        Bundle args = new Bundle();
        args.putLong(QUIZ_ID_ARG, quizId);
        args.putDouble(SCORE_ARG, score);

        CompletedQuizFragment fragment = new CompletedQuizFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        score = getArguments().getDouble(SCORE_ARG);
        quiz = QuizMapStorage.getInstance().getQuiz(getArguments().getLong(QUIZ_ID_ARG));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_completed_quiz, container, false);

        quizNameTextView = v.findViewById(R.id.completed_quiz_name);
        quizNameTextView.setText(quiz.getName());
        quizDateTextView= v.findViewById(R.id.completed_quiz_date);
        quizDateTextView.setText(Calendar.getInstance().getTime().toString());
        quizScoreTextView = v.findViewById(R.id.completed_quiz_score);
        quizScoreTextView.setText(Double.toString(score));

        shareButton = v.findViewById(R.id.share_completed_quiz);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompletedQuizDialogFragment dialog = new ShareCompletedQuizDialogFragment();
                dialog.setTargetFragment(CompletedQuizFragment.this, SHARE_QUIZ_REQUEST_CODE);
                dialog.show(getFragmentManager(), SHARE_QUIZ_TAG);
            }
        });

        return v;
    }

    @Override
    public void onEmailButtonClicked() {
        ShareEmailFragment fragment = ShareEmailFragment.newInstance(quiz.getName(), score);
        swapFragment(fragment);
    }

    @Override
    public void onSMSButtonClicked() {
        ShareSMSFragment fragment = ShareSMSFragment.newInstance(quiz.getName(), score);
        swapFragment(fragment);
    }

    private void swapFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
