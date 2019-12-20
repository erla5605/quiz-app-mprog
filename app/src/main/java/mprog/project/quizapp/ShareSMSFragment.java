package mprog.project.quizapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class ShareSMSFragment extends Fragment {

    private static final String QUIZ_NAME_ARG = "quiz name";
    private static final String SCORE_ARG = "score";

    private static final int SMS_PERMISSION_REQUEST = 200;

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE
    };

    private EditText toEditText;
    private TextView bodyEditText;

    private Button sendButton;

    private SmsManager smsManger;

    private String quizName;
    private double score;

    public static ShareSMSFragment newInstance(String quizName, double score) {
        Bundle args = new Bundle();
        args.putString(QUIZ_NAME_ARG, quizName);
        args.putDouble(SCORE_ARG, score);

        ShareSMSFragment fragment = new ShareSMSFragment();
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
        View v = inflater.inflate(R.layout.fragment_share_sms, container, false);

        toEditText = v.findViewById(R.id.sms_to_edit_text);
        bodyEditText = v.findViewById(R.id.sms_body_edit_text);
        bodyEditText.setText(getMessageText());

        sendButton = v.findViewById(R.id.sms_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission()) {
                    sendSMS();
                } else {
                    requestPermissions(PERMISSIONS, SMS_PERMISSION_REQUEST);
                }
            }
        });

        return v;
    }

    private String getMessageText() {
        return String.format(getString(R.string.share_completed_quiz_text), quizName, score);
    }

    private void sendSMS() {
        smsManger = SmsManager.getDefault();
        if (smsManger == null) {
            Toast.makeText(getActivity(), R.string.sms_manager_fail, Toast.LENGTH_SHORT).show();
            return;
        }
        String smsRecipient = toEditText.getText().toString();
        smsManger.sendTextMessage(smsRecipient, null, getMessageText(), null, null);
        getActivity().onBackPressed();
    }

    private boolean hasPermission() {
        return ContextCompat.checkSelfPermission(getActivity(), PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SMS_PERMISSION_REQUEST) {
            if (isPermissionGranted(grantResults)) {
                sendSMS();
            } else {
                Toast.makeText(getActivity(), R.string.denied_permission, Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isPermissionGranted(@NonNull int[] grantResults) {
        for (int i : grantResults) {
            if (i != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
