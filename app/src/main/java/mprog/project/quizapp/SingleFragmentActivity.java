package mprog.project.quizapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

// Abstract class for all activities which only shows a single fragment.
public abstract class SingleFragmentActivity extends AppCompatActivity {

    // OnCreate, starts the fragment and with the use of the fragment manager.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            manager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    // Method to provide the onCreate method with the correct fragment for the activity.
    protected abstract Fragment createFragment();
}
