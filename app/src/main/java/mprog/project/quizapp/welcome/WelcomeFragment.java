package mprog.project.quizapp.welcome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.List;

import mprog.project.quizapp.QuizListActivity;
import mprog.project.quizapp.R;

public class WelcomeFragment extends Fragment {

    private static final int PHOTO_REQUEST_CODE = 200;

    private static final String AUTHORITY_NAME = "mprog.project.quizapp.fileprovider";
    private static final String NAME_SAVED = "name";

    private ImageView photoImageView;
    private FloatingActionButton addPhotoButton;

    private String photoFileName = "quizappphotofile";
    private File photoFile;

    private EditText nameEditText;

    private Button startButton;

    // OnCreate, Gets the photofile
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File filesDir = getActivity().getFilesDir();
        photoFile = new File(filesDir, photoFileName);
    }

    /*  OnCreateView, sets up image view, edit text and buttons.
        Gets the user name from SharedPreferences if available, set the photo saved if available.*/
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);

        photoImageView = v.findViewById(R.id.photo_image_view);

        if (photoFile != null && photoFile.exists()) {
            setPhoto();
        }

        addPhotoButton = v.findViewById(R.id.add_photo_button);
        setUpAddPhotoButton();

        nameEditText = v.findViewById(R.id.enter_name_edit_text);
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String name = preferences.getString(NAME_SAVED, null);
        if (name != null) {
            nameEditText.setText(name);
        }

        startButton = v.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            // Checks that user has entered a name and starts the QuizListActivity if it has.
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                if (!name.isEmpty()) {
                    saveName(name);
                    startActivity(new Intent(getActivity(), QuizListActivity.class));
                } else {
                    Toast toast = Toast.makeText(getActivity(), R.string.no_name_text, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        return v;
    }

    // Sets up the add photo button.
    private void setUpAddPhotoButton() {
        final Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            // Starts the intent to take a photo with the correct permissions granted.
            @Override
            public void onClick(View v) {
                if (takePhoto.resolveActivity(getActivity().getPackageManager()) != null) {
                    Uri uri = FileProvider.getUriForFile(getActivity(), AUTHORITY_NAME, photoFile);
                    takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                    List<ResolveInfo> resolveInfos = getActivity().getPackageManager().queryIntentActivities(takePhoto, PackageManager.MATCH_DEFAULT_ONLY);

                    for (ResolveInfo info : resolveInfos) {
                        getActivity().grantUriPermission(info.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }

                    startActivityForResult(takePhoto, PHOTO_REQUEST_CODE);
                }
            }
        });
    }

    // Saves the users name.
    private void saveName(String name) {
        getActivity()
                .getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putString(NAME_SAVED, name)
                .apply();
    }

    // Handles the result from the intent to take a photo, revokes the permissions.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PHOTO_REQUEST_CODE) {
            Uri uri = FileProvider.getUriForFile(getActivity(), AUTHORITY_NAME, photoFile);

            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            setPhoto();
        }
    }

    // Sets the photo taken to the image view.
    private void setPhoto() {
        Bitmap bm = createBitmap();
        RoundedBitmapDrawable rounded = RoundedBitmapDrawableFactory.create(getResources(), bm);
        rounded.setCircular(true);
        photoImageView.setImageDrawable(rounded);
    }

    // Creates the bitmap for the image view.
    private Bitmap createBitmap() {
        int targetWidth = photoImageView.getWidth();
        int targetHeight = photoImageView.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        float photoWidth = options.outWidth;
        float photoHeight = options.outHeight;

        int inSampleSize = 1;
        if (photoWidth > targetWidth || photoHeight > targetHeight) {
            float widthScale = photoWidth / targetWidth;
            float heightScale = photoHeight / targetHeight;

            inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(photoFile.getPath(), options);
    }
}
