package mprog.project.quizapp.quizcomplete;

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

import mprog.project.quizapp.R;

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

    // Creates and returns a ShareEmailFragment instance with arguments for quiz name and quiz score.
    public static ShareEmailFragment newInstance(String quizName, double score) {
        Bundle args = new Bundle();
        args.putString(QUIZ_NAME_ARG, quizName);
        args.putDouble(SCORE_ARG, score);

        ShareEmailFragment fragment = new ShareEmailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // OnCreate gets the quiz name and score from args and set them in the fragment.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quizName = getArguments().getString(QUIZ_NAME_ARG);
        score = getArguments().getDouble(SCORE_ARG);
    }

    // OnCreateView creates the view, sets up the edit texts and buttons.
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
            // Calls on the sendEmail method and then closes the fragment.
            @Override
            public void onClick(View v) {
                sendEmail();
                getActivity().onBackPressed();
            }
        });

        editButton = v.findViewById(R.id.edit_email_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            // Enables editing of the email body.
            @Override
            public void onClick(View v) {
                editing = true;
                subjectEditText.setEnabled(editing);
                bodyEditText.setEnabled(editing);
            }
        });
        return v;
    }

    // Get the default message string for the body.
    private String getMessageText() {
        return String.format(getString(R.string.share_completed_quiz_text),quizName,score);
    }

    // Creates and intent and to send the email, recipients (splits by semicolon), subjext and body.
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
