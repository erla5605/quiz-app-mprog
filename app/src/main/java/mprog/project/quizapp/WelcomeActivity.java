package mprog.project.quizapp;

import androidx.fragment.app.Fragment;

public class WelcomeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new WelcomeFragment();
    }
}
