package mprog.project.quizapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mprog.project.quizapp.model.Quiz;
import mprog.project.quizapp.quizcreation.CreateQuizActivity;
import mprog.project.quizapp.quizcreation.CreateQuizFragment;
import mprog.project.quizapp.storage.QuizMapStorage;

public class QuizListFragment extends Fragment {

    private static final int CREATE_QUIZ_REQUEST_CODE = 200;
    private static final String TAG = "QuizAPP";

    private RecyclerView quizListRecyclerView;
    private QuizAdapter quizAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz_list, container, false);

        quizListRecyclerView = v.findViewById(R.id.quiz_recycler_view);
        quizListRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        quizAdapter = new QuizAdapter(QuizMapStorage.getInstance().getQuizzes());
        quizListRecyclerView.setAdapter(quizAdapter);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_quiz_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_quiz_menu_item:
                startActivityForResult(CreateQuizActivity.newIntent(getActivity()),CREATE_QUIZ_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private class QuizHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView quizNameTextView;
        private TextView quizDescriptionTextView;

        private Quiz quiz;

        public QuizHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_quiz_item, parent, false));

            quizNameTextView = itemView.findViewById(R.id.item_quiz_name_text_view);
            quizDescriptionTextView = itemView.findViewById(R.id.item_quiz_description_text_view);

            itemView.setOnClickListener(this);
        }

        public void bind(Quiz quiz) {
            this.quiz = quiz;
            quizNameTextView.setText(quiz.getName());
            quizDescriptionTextView.setText(quiz.getDescription());
        }

        @Override
        public void onClick(View v) {
            Intent intent = QuizActivity.newIntent(getActivity(), quiz.getId());
            startActivity(intent);
        }
    }

    private class QuizAdapter extends RecyclerView.Adapter<QuizHolder> {

        private List<Quiz> quizzes;

        public QuizAdapter(List<Quiz> quizzes) {
            this.quizzes = quizzes;
        }

        @NonNull
        @Override
        public QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new QuizHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull QuizHolder holder, int position) {
            holder.bind(quizzes.get(position));
        }

        @Override
        public int getItemCount() {
            return quizzes.size();
        }

        public void setQuizzes(List<Quiz> quizzes) {
            this.quizzes = quizzes;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CREATE_QUIZ_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if(data.getBooleanExtra(CreateQuizFragment.QUIZ_RESULT_EXTRA, false)){
                quizAdapter.setQuizzes(QuizMapStorage.getInstance().getQuizzes());
                quizAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), R.string.failed_quiz_creation, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
