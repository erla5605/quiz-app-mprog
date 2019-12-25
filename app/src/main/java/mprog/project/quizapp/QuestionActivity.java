package mprog.project.quizapp;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import mprog.project.quizapp.model.Question;

public class QuestionActivity extends SingleFragmentActivity {

    private static final String QUESTION_EXTRA = "question";

    public static Intent newIntent(Context context, Question question){
        Intent intent = new Intent(context, QuestionActivity.class);
        intent.putExtra(QUESTION_EXTRA, question);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Question question = getIntent().getParcelableExtra(QUESTION_EXTRA);
        if(question == null){
            throw new RuntimeException("Could not get question");
        }

        return QuestionFragment.newInstance(question);
    }
}
