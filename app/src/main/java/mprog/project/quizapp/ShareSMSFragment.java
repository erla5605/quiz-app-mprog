package mprog.project.quizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ShareSMSFragment extends Fragment {

    private static final String SCORE_ARG = "score";
    private static final String QUIZ_NAME_ARG = "quiz name";

    private EditText toEditText;
    private EditText bodyEditText;

    private Button sendButton;

    public static ShareSMSFragment newInstance(String quizName, double score) {
        Bundle args = new Bundle();
        args.putString(QUIZ_NAME_ARG, quizName);
        args.putDouble(SCORE_ARG, score);

        ShareSMSFragment fragment = new ShareSMSFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_share_sms, container, false);

        toEditText = v.findViewById(R.id.sms_to_edit_text);
        bodyEditText= v.findViewById(R.id.sms_body_edit_text);

        sendButton = v.findViewById(R.id.sms_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "SMS SENT", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });

        return v;
    }
}
