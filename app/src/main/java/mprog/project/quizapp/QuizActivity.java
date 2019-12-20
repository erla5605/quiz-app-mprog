package mprog.project.quizapp;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class QuizActivity extends SingleFragmentActivity {

    private static final String QUIZ_ID_EXTRA = "quiz id";

    public static Intent newIntent(Context context, Long quizId){
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(QUIZ_ID_EXTRA, quizId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Long id = getIntent().getLongExtra(QUIZ_ID_EXTRA, -1);
        if(id == -1){
            throw new RuntimeException("Could not get Quiz Id");
        }

        return QuizFragment.newInstance(id);
    }

}
