package mprog.project.quizapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CompleteQuizDialogFragment extends DialogFragment {

    interface CompleteQuizDialogListener{
        void onYesButtonClicked(DialogFragment dialog);
        void onCancelButtonClicked(DialogFragment dialog);
    }

    CompleteQuizDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.complete_quiz_dialog);
        builder.setNegativeButton(R.string.cancel_dialog_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // No action on cancel.
            }
        });
        builder.setPositiveButton(R.string.yes_dialog_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onYesButtonClicked(CompleteQuizDialogFragment.this);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CompleteQuizDialogListener) getTargetFragment();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement CompleteQuizDialogListener!");
        }
    }
}
