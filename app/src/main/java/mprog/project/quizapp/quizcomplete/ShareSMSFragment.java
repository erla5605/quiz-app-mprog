package mprog.project.quizapp.quizcomplete;

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

import mprog.project.quizapp.R;

public class ShareSMSFragment extends Fragment {

    private static final String QUIZ_NAME_ARG = "quiz name";
    private static final String SCORE_ARG = "score";

    private static final int SMS_PERMISSION_REQUEST = 200;

    // String array for permissions required to send sms.
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

    // Creates and returns a ShareSMSFragment instance with arguments for quiz name and quiz score.
    public static ShareSMSFragment newInstance(String quizName, double score) {
        Bundle args = new Bundle();
        args.putString(QUIZ_NAME_ARG, quizName);
        args.putDouble(SCORE_ARG, score);

        ShareSMSFragment fragment = new ShareSMSFragment();
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

    // OnCreateView creates the view, sets up the text views and button.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_share_sms, container, false);

        toEditText = v.findViewById(R.id.sms_to_edit_text);
        bodyEditText = v.findViewById(R.id.sms_body_edit_text);
        bodyEditText.setText(getMessageText());

        sendButton = v.findViewById(R.id.sms_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            // Calls on the sendSMS if user has granted permission, else request the permission from the user.
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

    // Get the default message string.
    private String getMessageText() {
        return String.format(getString(R.string.share_completed_quiz_text), quizName, score);
    }

/*  Get SmsManager and uses it to send the sms to the recipient and after that closes the fragment.
    Checks that phone number is not missing*/
    private void sendSMS() {
        smsManger = SmsManager.getDefault();
        if (smsManger == null) {
            Toast.makeText(getActivity(), R.string.sms_manager_fail, Toast.LENGTH_SHORT).show();
            return;
        }
        String smsRecipient = toEditText.getText().toString();
        if(smsRecipient.isEmpty()){
            Toast.makeText(getActivity(), getString(R.string.no_phone_number), Toast.LENGTH_SHORT).show();
            return;
        }
        smsManger.sendTextMessage(smsRecipient, null, getMessageText(), null, null);
        getActivity().onBackPressed();
    }

    // Checks if the user has granted the permissions to send sms.
    private boolean hasPermission() {
        return ContextCompat.checkSelfPermission(getActivity(), PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED;
    }

    // Requests the user of the permissions needed to send sms.
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

    // Checks if user has granted permission.
    private boolean isPermissionGranted(@NonNull int[] grantResults) {
        for (int i : grantResults) {
            if (i != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
