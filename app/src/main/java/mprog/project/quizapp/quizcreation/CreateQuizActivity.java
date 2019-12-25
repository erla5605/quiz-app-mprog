package mprog.project.quizapp.quizcreation;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import mprog.project.quizapp.SingleFragmentActivity;

public class CreateQuizActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        return new Intent(context, CreateQuizActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return new CreateQuizFragment();
    }
}
