package mprog.project.quizapp.quizcomplete;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.util.UUID;

import mprog.project.quizapp.SingleFragmentActivity;

public class CompletedQuizActivity extends SingleFragmentActivity {

    private static final String QUIZ_ID_EXTRA = "quiz_id";
    private static final String SCORE_EXTRA = "score";

    public static Intent newIntent(Context context, UUID quizId, double score){
        Intent intent = new Intent(context, CompletedQuizActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable(QUIZ_ID_EXTRA, quizId);
        extras.putDouble(SCORE_EXTRA, score);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Bundle extras = getIntent().getExtras();
        UUID quizId = (UUID) extras.getSerializable(QUIZ_ID_EXTRA);
        double score = extras.getDouble(SCORE_EXTRA);

        return CompletedQuizFragment.newInstance(quizId, score);
    }
}
