package mprog.project.quizapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CompletedQuizActivity extends AppCompatActivity {

    private static final String QUIZ_ID_EXTRA = "quiz_id";
    private static final String SCORE_EXTRA = "score";

    public static Intent newIntent(Context context, Long quizId, double score){
        Intent intent = new Intent(context, CompletedQuizActivity.class);
        Bundle extras = new Bundle();
        extras.putLong(QUIZ_ID_EXTRA, quizId);
        extras.putDouble(SCORE_EXTRA, score);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            Bundle extras = getIntent().getExtras();
            Long quizId = extras.getLong(QUIZ_ID_EXTRA);
            double score = extras.getDouble(SCORE_EXTRA);

            fragment = CompletedQuizFragment.newInstance(quizId, score);
            manager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

    }
}
