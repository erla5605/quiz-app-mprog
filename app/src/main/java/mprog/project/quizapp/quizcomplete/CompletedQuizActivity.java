package mprog.project.quizapp.quizcomplete;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import mprog.project.quizapp.SingleFragmentActivity;

public class CompletedQuizActivity extends SingleFragmentActivity {

    private static final String QUIZ_NAME_EXTRA = "quiz_id";
    private static final String SCORE_EXTRA = "score";

    // Creates and returns an intent for CompletedQuizActivity, with extras for quiz name and quiz score.
    public static Intent newIntent(Context context, String quizName, double score){
        Intent intent = new Intent(context, CompletedQuizActivity.class);
        Bundle extras = new Bundle();
        extras.putString(QUIZ_NAME_EXTRA, quizName);
        extras.putDouble(SCORE_EXTRA, score);
        intent.putExtras(extras);
        return intent;
    }

    // Creates and returns CompletedQuizFragment, set quiz name and quiz score extra to the fragment.
    @Override
    protected Fragment createFragment() {
        Bundle extras = getIntent().getExtras();
        String quizName = extras.getString(QUIZ_NAME_EXTRA);
        double score = extras.getDouble(SCORE_EXTRA);

        return CompletedQuizFragment.newInstance(quizName, score);
    }
}
