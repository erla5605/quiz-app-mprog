package mprog.project.quizapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

import mprog.project.quizapp.model.Answer;
import mprog.project.quizapp.model.Question;
import mprog.project.quizapp.storage.QuestionMapStorage;

public class QuestionFragment extends Fragment {

    public static final String ANSWER_EXTRA = "answer_boolean";
    public static final String QUESTION_EXTRA = "answered_question_id";
    public static final String QUESTION_ID_ARG = "question_id";
    private static final int TTS_REQUEST_CODE = 200;

    private Question question;
    private List<Answer> answers;
    private List<Integer> radioButtonIds = new ArrayList<>();

    private TextView questionTextView;
    private ImageButton playVideoButton;
    private ImageButton ttsButton;

    private RadioGroup answersRadioGroup;
    private Button answerQuestionButton;

    private TextToSpeech tts;

    public static QuestionFragment newInstance(Long quizId) {
        Bundle args = new Bundle();
        args.putLong(QUESTION_ID_ARG, quizId);

        QuestionFragment questionFragment = new QuestionFragment();
        questionFragment.setArguments(args);
        return questionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Long questionId = getArguments().getLong(QUESTION_ID_ARG);
        question = QuestionMapStorage.getInstance().getQuestion(questionId);
        answers = question.getAnswers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, container, false);

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
                    Toast.makeText(getContext(), "NO ANSWER", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void createRadioButtonsForAnswers(View v) {
        for (Answer answer : answers) {
            RadioButton rButton = new RadioButton(v.getContext());
            int radioButtonId = v.generateViewId();
            rButton.setId(radioButtonId);
            radioButtonIds.add(radioButtonId);
            rButton.setText(answer.getAnswerText());
            rButton.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            answersRadioGroup.addView(rButton);
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
        extras.putLong(QUESTION_EXTRA, question.getId());
        Intent intent = new Intent().putExtras(extras);
        getActivity().setResult(Activity.RESULT_OK, intent);
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
                    tts.speak(question.getQuestionText(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    private void installTTSData() {
        Intent intent = new Intent().setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(intent);
    }
}
