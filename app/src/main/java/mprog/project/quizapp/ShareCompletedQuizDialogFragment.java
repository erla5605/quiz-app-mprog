package mprog.project.quizapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ShareCompletedQuizDialogFragment extends DialogFragment {

    private static final int EMAIL_OPTION = 0;
    private static final int SMS_OPTION = 1;

    interface ShareCompletedQuizDialogListener {
        void onEmailButtonClicked();

        void onSMSButtonClicked();
    }

    private ShareCompletedQuizDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.share_option_dialog);
        builder.setItems(R.array.share_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case EMAIL_OPTION:
                        listener.onEmailButtonClicked();
                        break;
                    case SMS_OPTION:
                        listener.onSMSButtonClicked();
                        break;
                    default:
                        dialog.cancel();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_dialog_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ShareCompletedQuizDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().getClass().getName() +
                    " must implement ShareCompletedQuizDialogListener!");
        }


    }
}
