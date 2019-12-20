package mprog.project.quizapp;

import androidx.fragment.app.Fragment;

public class QuizActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new QuizFragment();
    }

}
