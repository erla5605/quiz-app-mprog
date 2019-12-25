package mprog.project.quizapp.quizcreation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import mprog.project.quizapp.R;

public class AddVideoDialogFragment extends DialogFragment {

    interface AddVideoDialogFragmentListener {
        void addVideo(String videoUrl);
    }

    private EditText videoUrlEditText;

    private AddVideoDialogFragmentListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoUrlEditText = new EditText(getActivity());
        videoUrlEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        videoUrlEditText.setHint(R.string.add_video_hint);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_video)
                .setView(videoUrlEditText)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String videoUrl = videoUrlEditText.getText().toString();
                        if(videoUrl.isEmpty() || !URLUtil.isValidUrl(videoUrl) || !videoUrl.contains("youtube")){
                            Toast.makeText(getActivity(), "No valid url", Toast.LENGTH_SHORT).show();
                        } else {
                            listener.addVideo(videoUrl);
                        }
                    }
                })
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (AddVideoDialogFragmentListener) getTargetFragment();
        } catch (ClassCastException e){
            throw new ClassCastException(getTargetFragment().getClass().getName()
                    + " must implement AddVideoDialogFragmentListener!");
        }
    }
}
