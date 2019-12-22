package mprog.project.quizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import mprog.project.quizapp.model.Question;

public class CreateQuizFragment extends Fragment {

    private EditText nameEditText;
    private EditText descriptionEditText;

    private RecyclerView questionRecyclerView;
    private QuestionAdapter questionAdapter;

    private FloatingActionButton addQuestionButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_quiz, container, false);

        nameEditText = v.findViewById(R.id.quiz_name_edit_text);
        descriptionEditText = v.findViewById(R.id.quiz_description_edit_text);

        questionRecyclerView = v.findViewById(R.id.new_question_recycler_view);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        questionAdapter = new QuestionAdapter(new ArrayList<Question>());
        questionRecyclerView.setAdapter(questionAdapter);

        addQuestionButton = v.findViewById(R.id.add_question_floating_button);
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateQuestionFragment fragment = new CreateQuestionFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_create, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_quiz_item:
                Toast.makeText(getActivity(), "DONE", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Question question;
        private TextView questionText;

        public QuestionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_new_question_item, parent, false));

            questionText = itemView.findViewById(R.id.item_question_text);

            itemView.setOnClickListener(this);
        }

        public void bind(Question question) {
            this.question = question;
            questionText.setText(question.getQuestionText());
        }

        @Override
        public void onClick(View v) {
        }
    }

    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {

        private List<Question> questions;

        public QuestionAdapter(List<Question> questions) {
            this.questions = questions;
        }

        @NonNull
        @Override
        public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new QuestionHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {
            Question question = questions.get(position);
            holder.bind(question);
        }

        @Override
        public int getItemCount() {
            return questions.size();
        }

        public void add(Question question) {
            questions.add(question);
        }
    }
}
