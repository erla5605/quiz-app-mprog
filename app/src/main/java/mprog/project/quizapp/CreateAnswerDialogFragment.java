package mprog.project.quizapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CreateAnswerDialogFragment extends DialogFragment {

    interface CreateAnswerDialogFragmentListener{
        void createAnswer(String answerText);
    }

    private EditText answerEditText;

    private CreateAnswerDialogFragmentListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        answerEditText = new EditText(getActivity());
        answerEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        answerEditText.setHint(R.string.enter_answer_hint);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.create_answer_dialog_title)
                .setView(answerEditText)
                .setNegativeButton(R.string.cancel_dialog_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.create_answer, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String answerText = answerEditText.getText().toString();
                        if(answerText.isEmpty()){
                            Toast.makeText(getActivity(), "Answer text required to create answer", Toast.LENGTH_SHORT).show();
                        } else {
                            listener.createAnswer(answerText);
                        }
                    }
                })
                .create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (CreateAnswerDialogFragmentListener) getTargetFragment();
        } catch (ClassCastException e){
            throw new ClassCastException(getTargetFragment().getClass().getName()
                    + " must implement setCorrectAnswerDialogListener!");
        }

    }
}
