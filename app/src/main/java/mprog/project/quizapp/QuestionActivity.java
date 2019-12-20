package mprog.project.quizapp;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class QuestionActivity extends SingleFragmentActivity {

    private static final String QUESTION_ID_EXTRA = "mprog.project.question_id";

    public static Intent newIntent(Context context, Long questionId){
        Intent intent = new Intent(context, QuestionActivity.class);
        intent.putExtra(QUESTION_ID_EXTRA, questionId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Long questionId = getIntent().getLongExtra(QUESTION_ID_EXTRA, -1);
        if(questionId == -1){
            throw new RuntimeException("Could not get Question Id");
        }

        return QuestionFragment.newInstance(questionId);
    }
}
