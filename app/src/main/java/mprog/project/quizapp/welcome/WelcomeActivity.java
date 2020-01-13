package mprog.project.quizapp.welcome;

import androidx.fragment.app.Fragment;

import mprog.project.quizapp.SingleFragmentActivity;

public class WelcomeActivity extends SingleFragmentActivity {

    // Creates WelcomeFragment
    @Override
    protected Fragment createFragment() {
        return new WelcomeFragment();
    }
}
