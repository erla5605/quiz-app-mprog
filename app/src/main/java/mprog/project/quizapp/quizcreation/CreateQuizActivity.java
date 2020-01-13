package mprog.project.quizapp.quizcreation;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import mprog.project.quizapp.R;
import mprog.project.quizapp.SingleFragmentActivity;
import mprog.project.quizapp.model.Question;

public class CreateQuizActivity extends SingleFragmentActivity implements CreateQuestionFragment.CreateQuestionListener {

    // Creates an new intent for the CreateQuizActivity
    public static Intent newIntent(Context context) {
        return new Intent(context, CreateQuizActivity.class);
    }

    // Creates a new CreateQuizFragment.
    @Override
    protected Fragment createFragment() {
        return new CreateQuizFragment();
    }

    // Set itself as the listener for CreateQuestionFragment for question creation.
    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof CreateQuestionFragment) {
            CreateQuestionFragment createQuestionFragment = (CreateQuestionFragment) fragment;
            createQuestionFragment.setCreateQuestionListener(this);
        }
    }

    // Method from CreateQuestionListener interface calls on the CreateQuizFragment to add the newly created question.
    @Override
    public void questionCreated(Question question) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null && fragment instanceof CreateQuizFragment) {
            ((CreateQuizFragment) fragment).questionCreated(question);
        }
    }
}
