package mprog.project.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ShareEmailFragment extends Fragment {

    private static final String QUIZ_NAME_ARG = "quiz name";
    private static final String SCORE_ARG = "score";

    private EditText recipientEditText;
    private EditText subjectEditText;
    private EditText bodyEditText;

    private Button sendButton;
    private Button editButton;

    private String quizName;
    private double score;

    private boolean editing;

    public static ShareEmailFragment newInstance(String quizName, double score) {
        Bundle args = new Bundle();
        args.putString(QUIZ_NAME_ARG, quizName);
        args.putDouble(SCORE_ARG, score);

        ShareEmailFragment fragment = new ShareEmailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quizName = getArguments().getString(QUIZ_NAME_ARG);
        score = getArguments().getDouble(SCORE_ARG);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_share_email, container, false);

        recipientEditText = v.findViewById(R.id.recipient_edit_text);
        subjectEditText = v.findViewById(R.id.email_subject_edit_text);
        subjectEditText.setText(R.string.subject_email_text);
        subjectEditText.setEnabled(editing);
        bodyEditText = v.findViewById(R.id.email_body_edit_text);
        bodyEditText.setText(getMessageText());
        bodyEditText.setEnabled(editing);

        sendButton = v.findViewById(R.id.email_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
                getActivity().onBackPressed();
            }
        });

        editButton = v.findViewById(R.id.edit_email_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editing = true;
                subjectEditText.setEnabled(editing);
                bodyEditText.setEnabled(editing);
            }
        });
        return v;
    }

    private String getMessageText() {
        return String.format(getString(R.string.share_completed_quiz_text),quizName,score);
    }

    private void sendEmail() {
        Intent intent = new  Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        String[] recipients = recipientEditText.getText().toString().split(";");
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subjectEditText.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, bodyEditText.getText());
        startActivity(intent);
    }
}
