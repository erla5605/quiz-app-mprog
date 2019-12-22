package mprog.project.quizapp;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class CreateQuizActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        return new Intent(context, CreateQuizActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return new CreateQuizFragment();
    }
}
