package mprog.project.quizapp;

import androidx.fragment.app.Fragment;

public class QuizListActivity extends SingleFragmentActivity {

    // Creates a QuizListFragment
    @Override
    protected Fragment createFragment() {
        return new QuizListFragment();
    }
}
