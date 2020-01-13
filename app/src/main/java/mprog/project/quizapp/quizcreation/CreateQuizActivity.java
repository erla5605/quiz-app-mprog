package mprog.project.quizapp.quizcreation;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import mprog.project.quizapp.R;
import mprog.project.quizapp.SingleFragmentActivity;
import mprog.project.quizapp.model.Question;

public class CreateQuizActivity extends SingleFragmentActivity implements CreateQuestionFragment.CreateQuestionListener {

    private static final String TAG = "QuizAPP";

    public static Intent newIntent(Context context) {
        return new Intent(context, CreateQuizActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return new CreateQuizFragment();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof CreateQuestionFragment) {
            CreateQuestionFragment createQuestionFragment = (CreateQuestionFragment) fragment;
            createQuestionFragment.setCreateQuestionListener(this);
        }
    }

    @Override
    public void questionCreated(Question question) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null && fragment instanceof CreateQuizFragment) {
            ((CreateQuizFragment) fragment).questionCreated(question);

        }
    }
}
