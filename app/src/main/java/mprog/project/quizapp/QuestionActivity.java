package mprog.project.quizapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class QuestionActivity extends AppCompatActivity {

    private static final String QUESTION_ID_EXTRA = "mprog.project.question_id";

    public static Intent newIntent(Context context, Long questionId){
        Intent intent = new Intent(context, QuestionActivity.class);
        intent.putExtra(QUESTION_ID_EXTRA, questionId);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment questionFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(questionFragment == null){
            Long questionId = getIntent().getLongExtra(QUESTION_ID_EXTRA, -1);
            if(questionId == -1){
                throw new RuntimeException("Could not get questionId");
            }

            questionFragment = QuestionFragment.newInstance(questionId);
            fragmentManager.beginTransaction().add(R.id.fragment_container, questionFragment).commit();
        }
    }
}
