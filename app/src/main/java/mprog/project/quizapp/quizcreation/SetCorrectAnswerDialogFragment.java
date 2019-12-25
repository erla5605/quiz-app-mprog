package mprog.project.quizapp.quizcreation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import mprog.project.quizapp.R;

public class SetCorrectAnswerDialogFragment extends DialogFragment {

    interface setCorrectAnswerDialogListener {
        void setCorrectAnswer(int answerPosition);
    }

    private static final String ANSWER_POSITION_ARGS = "answer position";


    public static final SetCorrectAnswerDialogFragment newInstance(int answerPosition){
        Bundle args = new Bundle();
        args.putInt(ANSWER_POSITION_ARGS, answerPosition);

        SetCorrectAnswerDialogFragment fragment = new SetCorrectAnswerDialogFragment();
        fragment.setArguments(args);
        return fragment;
        
    }

    private int answerPosition;

    private setCorrectAnswerDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        answerPosition = getArguments().getInt(ANSWER_POSITION_ARGS);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.set_correct_answer_dialog)
                .setNegativeButton(R.string.cancel_dialog_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.correct_answer, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.setCorrectAnswer(answerPosition);
                    }
                }).create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (setCorrectAnswerDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().getClass().getName()
                    + " must implement setCorrectAnswerDialogListener!");
        }
    }
}
