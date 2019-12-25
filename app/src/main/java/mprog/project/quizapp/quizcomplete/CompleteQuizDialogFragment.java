package mprog.project.quizapp.quizcomplete;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import mprog.project.quizapp.R;

public class CompleteQuizDialogFragment extends DialogFragment {

    public interface CompleteQuizDialogListener{

        void onYesButtonClicked();
    }

    private CompleteQuizDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.complete_quiz_dialog);
        builder.setNegativeButton(R.string.cancel_dialog_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        builder.setPositiveButton(R.string.yes_dialog_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onYesButtonClicked();
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
            throw new ClassCastException(getTargetFragment().getClass().getName()
                    + " must implement CompleteQuizDialogListener!");
        }
    }
}
