package mprog.project.quizapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment quizFragement = fragmentManager.findFragmentById(R.id.fragment_container);

        if(quizFragement == null){
            quizFragement = new QuizFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, quizFragement).commit();
        }
    }
}
