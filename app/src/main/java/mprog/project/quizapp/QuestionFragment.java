package mprog.project.quizapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mprog.project.quizapp.model.Answer;
import mprog.project.quizapp.model.Question;

public class QuestionFragment extends Fragment {

    public static final String ANSWER_EXTRA = "answer_boolean";
    public static final String QUESTION_EXTRA = "answered_question";
    public static final String QUESTION_ARG = "question";
    public static final String CHECKED_ANSWER_ID = "checked_answer";

    private static final int TTS_REQUEST_CODE = 200;

    private Question question;
    private List<Answer> answers;
    private List<Integer> radioButtonIds = new ArrayList<>();
    private UUID checkAnswerId;

    private TextView questionTextView;
    private ImageButton playVideoButton;
    private ImageButton ttsButton;

    private RadioGroup answersRadioGroup;
    private Button answerQuestionButton;

    private TextToSpeech tts;

    public static QuestionFragment newInstance(Question question) {
        Bundle args = new Bundle();
        args.putParcelable(QUESTION_ARG, question);

        QuestionFragment questionFragment = new QuestionFragment();
        questionFragment.setArguments(args);
        return questionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        question = getArguments().getParcelable(QUESTION_ARG);

        answers = question.getAnswers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, container, false);

        if(savedInstanceState != null){
            checkAnswerId = (UUID) savedInstanceState.getSerializable(CHECKED_ANSWER_ID);
        }

        questionTextView = v.findViewById(R.id.question_text_view);
        questionTextView.setText(question.getQuestionText());

        playVideoButton = v.findViewById(R.id.play_video_button);
        setUpPlayVideoButton();

        ttsButton = v.findViewById(R.id.tts_button);
        ttsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
                startActivityForResult(intent, TTS_REQUEST_CODE);
            }
        });

        answersRadioGroup = v.findViewById(R.id.answers_radio_group);
        createRadioButtonsForAnswers(v);

        answerQuestionButton = v.findViewById(R.id.answer_question_button);
        answerQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = answersRadioGroup.getCheckedRadioButtonId(); // Returns -1 if no button checked.
                if (id != -1) {
                    setQuestionResult(id);
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), R.string.missing_answer, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void createRadioButtonsForAnswers(View v) {
        for (Answer answer : answers) {
            RadioButton rButton = new RadioButton(v.getContext());
            int radioButtonId = View.generateViewId();
            rButton.setId(radioButtonId);
            radioButtonIds.add(radioButtonId);
            rButton.setText(answer.getAnswerText());
            rButton.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            answersRadioGroup.addView(rButton);
            if(answer.getId().equals(checkAnswerId)){
                rButton.setChecked(true);
            }
        }
    }

    private void setUpPlayVideoButton() {
        if (question.getType() != Question.QuestionType.VIDEO) {
            playVideoButton.setVisibility(View.INVISIBLE);
        } else {
            playVideoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(question.getVideo());
                    uri = Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"));
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
            });
        }
    }

    private void setQuestionResult(int id) {
        int index = radioButtonIds.indexOf(id);
        boolean answeredCorrectly = answers.get(index).isCorrectAnswer();
        Bundle extras = new Bundle();
        extras.putBoolean(ANSWER_EXTRA, answeredCorrectly);
        extras.putParcelable(QUESTION_EXTRA, question);
        Intent intent = new Intent().putExtras(extras);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int id = answersRadioGroup.getCheckedRadioButtonId(); // Returns -1 if no button checked.
        if (id != -1) {
            UUID checkedAnswerId = answers.get(radioButtonIds.indexOf(id)).getId();
            outState.putSerializable(CHECKED_ANSWER_ID,checkedAnswerId);
        } else {
            outState.putSerializable(CHECKED_ANSWER_ID, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TTS_REQUEST_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                startTTS();
            } else {
                installTTSData();
            }
        }
    }

    private void startTTS() {
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    speak();
                }
            }
        });
    }

    private void speak() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(question.getQuestionText(), TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(question.getQuestionText(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void installTTSData() {
        Intent intent = new Intent().setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(intent);
    }
}
