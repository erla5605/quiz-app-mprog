package mprog.project.quizapp.quiz;

import androidx.fragment.app.Fragment;

import mprog.project.quizapp.SingleFragmentActivity;

public class QuizListActivity extends SingleFragmentActivity {

    // Creates a QuizListFragment
    @Override
    protected Fragment createFragment() {
        return new QuizListFragment();
    }
}
