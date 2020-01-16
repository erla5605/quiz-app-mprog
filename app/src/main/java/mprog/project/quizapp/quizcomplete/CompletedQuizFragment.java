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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import mprog.project.quizapp.R;


public class CompletedQuizFragment extends Fragment implements ShareCompletedQuizDialogFragment.ShareCompletedQuizDialogListener {

    private static final String QUIZ_NAME_ARG = "quiz_name";
    private static final String SCORE_ARG = "score";
    private static final String SHARE_QUIZ_TAG = "share quiz";
    private static final int SHARE_QUIZ_REQUEST_CODE = 200;

    private TextView quizNameTextView;
    private TextView quizDateTextView;
    private TextView quizScoreTextView;

    private Button shareButton;

    private String quizName;

    private double score;

    // Creates and returns a CompletedQuizFragment instance with arguments for quiz name and quiz score.
    public static CompletedQuizFragment newInstance(String name, double score) {
        Bundle args = new Bundle();
        args.putString(QUIZ_NAME_ARG, name);
        args.putDouble(SCORE_ARG, score);

        CompletedQuizFragment fragment = new CompletedQuizFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // OnCreate gets the quiz that had been completed with the name from the arguments.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        score = getArguments().getDouble(SCORE_ARG);
        quizName = getArguments().getString(QUIZ_NAME_ARG);
    }

    // OnCreateView creates the view, sets up the text views with the text from the quiz and share button.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_completed_quiz, container, false);

        quizNameTextView = v.findViewById(R.id.completed_quiz_name);
        quizNameTextView.setText(quizName);
        quizDateTextView= v.findViewById(R.id.completed_quiz_date);
        quizDateTextView.setText(getDateText());
        quizScoreTextView = v.findViewById(R.id.completed_quiz_score);
        quizScoreTextView.setText(Double.toString(score));

        shareButton = v.findViewById(R.id.share_completed_quiz);
        shareButton.setOnClickListener(new View.OnClickListener() {
            // Starts dialog fragment to pick between sharing result via sms or email.
            @Override
            public void onClick(View v) {
                ShareCompletedQuizDialogFragment dialog = new ShareCompletedQuizDialogFragment();
                dialog.setTargetFragment(CompletedQuizFragment.this, SHARE_QUIZ_REQUEST_CODE);
                dialog.show(getFragmentManager(), SHARE_QUIZ_TAG);
            }
        });

        return v;
    }

    // Creates the text string for the Date using yyyy-MM-dd HH:mm pattern.
    private String getDateText() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(Calendar.getInstance().getTime());
    }

    // Creates a new instance of ShareEmailFragment
    @Override
    public void onEmailButtonClicked() {
        ShareEmailFragment fragment = ShareEmailFragment.newInstance(quizName, score);
        swapFragment(fragment);
    }

    // Creates a new instance of ShareSmsFragment
    @Override
    public void onSMSButtonClicked() {
        ShareSMSFragment fragment = ShareSMSFragment.newInstance(quizName, score);
        swapFragment(fragment);
    }

    // Swaps the fragment to display the share fragment that was passed in.
    private void swapFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
