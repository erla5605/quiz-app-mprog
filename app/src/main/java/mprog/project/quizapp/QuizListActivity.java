package mprog.project.quizapp;

import androidx.fragment.app.Fragment;

public class QuizListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new QuizListFragment();
    }
}
