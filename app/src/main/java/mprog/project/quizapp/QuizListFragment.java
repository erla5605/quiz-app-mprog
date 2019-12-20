package mprog.project.quizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mprog.project.quizapp.model.Quiz;
import mprog.project.quizapp.storage.QuizMapStorage;

public class QuizListFragment extends Fragment {

    private RecyclerView quizListRecyclerView;
    private QuizAdapter quizAdapter;

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


    private class QuizHolder extends RecyclerView.ViewHolder {

        private TextView quizNameTextView;

        private Quiz quiz;

        public QuizHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_quiz_item, parent, false));

            quizNameTextView = itemView.findViewById(R.id.item_quiz_name_text_view);
        }

        public void bind(Quiz quiz) {
            this.quiz = quiz;
            quizNameTextView.setText(quiz.getName());
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

    }
}
