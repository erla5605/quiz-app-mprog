package mprog.project.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mprog.project.quizapp.model.Question;
import mprog.project.quizapp.model.Quiz;
import mprog.project.quizapp.storage.QuizMapStorage;

public class QuizFragment extends Fragment {

    private TextView quizDescriptionTextView;
    private RecyclerView questionRecyclerView;

    private QuestionAdapter questionAdapter;

    private Quiz quiz;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quiz = QuizMapStorage.getInstance().getQuizzes().get(0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);

        quizDescriptionTextView = v.findViewById(R.id.description_text_view);
        quizDescriptionTextView.setText(quiz.getDescription());

        questionRecyclerView = v.findViewById(R.id.question_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        questionRecyclerView.setLayoutManager(linearLayoutManager);

        questionAdapter = new QuestionAdapter(quiz.getQuestions());
        questionRecyclerView.setAdapter(questionAdapter);

        return v;
    }

    private class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Question question;
        private TextView questionText;

        public QuestionHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_question_item, parent, false));

            questionText = itemView.findViewById(R.id.item_question_text);
            itemView.setOnClickListener(this);
        }

        public void bind(Question question){
            this.question = question;
            questionText.setText(this.question.getQuestionText());
        }

        @Override
        public void onClick(View v) {
            Intent intent = QuestionActivity.newIntent(getActivity(), question.getId());
            startActivity(intent);
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
    }


}
