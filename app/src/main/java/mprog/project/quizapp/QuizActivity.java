package mprog.project.quizapp;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class QuizActivity extends SingleFragmentActivity {

    private static final String TAG = "QuizActivity";

    private static final String QUIZ_ID_EXTRA = "quiz id";

    public static Intent newIntent(Context context, UUID quizId){
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(QUIZ_ID_EXTRA, quizId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID id = (UUID) getIntent().getSerializableExtra(QUIZ_ID_EXTRA);
        if(id == null){
            throw new RuntimeException("Could not get Quiz Id");
        }

        return QuizFragment.newInstance(id);
    }

}
