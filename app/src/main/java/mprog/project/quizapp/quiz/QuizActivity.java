package mprog.project.quizapp.quiz;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.UUID;

import mprog.project.quizapp.SingleFragmentActivity;

public class QuizActivity extends SingleFragmentActivity {

    private static final String QUIZ_ID_EXTRA = "quiz id";

    // Creates intent for QuizActivity, with the quiz id as extra.
    public static Intent newIntent(Context context, UUID quizId){
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(QUIZ_ID_EXTRA, quizId);
        return intent;
    }

    // Creates the QuizFragment with the quiz id.
    @Override
    protected Fragment createFragment() {
        UUID id = (UUID) getIntent().getSerializableExtra(QUIZ_ID_EXTRA);
        if(id == null){
            throw new RuntimeException("Could not get Quiz Id");
        }

        return QuizFragment.newInstance(id);
    }

}
