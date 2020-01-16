package mprog.project.quizapp.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mprog.project.quizapp.model.Answer;
import mprog.project.quizapp.model.Question;
import mprog.project.quizapp.model.Quiz;

public class QuizApi {

    private static final String TAG = "QuizAPP";

    public interface QuizApiResponseListener {
        void errorResponse(String error);

        void quizListResponse(List<Quiz> quizzes);

        void quizResponse(Quiz quiz);

        void postResponse();
    }

    private QuizApiResponseListener listener;
    private Context context;

    public QuizApi(QuizApiResponseListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    private static final String API_URL = "http://10.0.2.2:8080/quizzes";

    public void getQuizzes() {
        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, API_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<Quiz> quizzes = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                quizzes.add(createQuizFromJson(response.getJSONObject(i)));
                            }
                            listener.quizListResponse(quizzes);
                        } catch (JSONException e) {
                            listener.errorResponse(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.errorResponse(error.getMessage());
            }
        });

        RequestQueueProvider.getInstance(context).addToRequestQueue(getRequest);
    }

    public void getQuiz(UUID id) {
        String url = API_URL + "/" + id;

        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Quiz quiz = createQuizFromJson(response);
                    Log.d(TAG, quiz.toString());
                    listener.quizResponse(quiz);
                } catch (JSONException e) {
                    listener.errorResponse(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.errorResponse(error.getMessage());
            }
        });
        RequestQueueProvider.getInstance(context).addToRequestQueue(getRequest);
    }

    private Quiz createQuizFromJson(JSONObject quizJson) throws JSONException {
        String id = quizJson.getString("id");
        String name = quizJson.getString("name");
        String description = quizJson.getString("description");
        ArrayList<Question> questions = getQuestions(quizJson);

        Quiz quiz = new Quiz();
        quiz.setId(UUID.fromString(id));
        quiz.setName(name);
        quiz.setDescription(description);
        quiz.setQuestions(questions);
        return quiz;
    }

    private ArrayList<Question> getQuestions(JSONObject quiz) throws JSONException {
        ArrayList<Question> questions = new ArrayList<>();
        JSONArray questionsJson = quiz.getJSONArray("questions");
        for (int x = 0; x < questionsJson.length(); x++) {
            JSONObject questionJson = questionsJson.getJSONObject(x);
            String questionId = questionJson.getString("id");
            String questionText = questionJson.getString("questionText");
            List<Answer> answers = getAnswers(questionJson);
            String type = questionJson.getString("type");
            String video = questionJson.getString("video");

            Question question = new Question();
            question.setId(UUID.fromString(questionId));
            question.setQuestionText(questionText);
            question.setAnswers(answers);
            question.setType(Question.QuestionType.valueOf(type));
            question.setVideo(video);

            questions.add(question);
        }

        return questions;
    }

    private List<Answer> getAnswers(JSONObject question) throws JSONException {
        List<Answer> answers = new ArrayList<>();

        JSONArray answersJson = question.getJSONArray("answers");
        for (int y = 0; y < answersJson.length(); y++) {
            JSONObject answerJson = answersJson.getJSONObject(y);

            String answerId = answerJson.getString("id");
            String answerText = answerJson.getString("answerText");
            boolean correctAnswer = answerJson.getBoolean("correctAnswer");

            Answer answer = new Answer();
            answer.setId(UUID.fromString(answerId));
            answer.setAnswerText(answerText);
            answer.setCorrectAnswer(correctAnswer);

            answers.add(answer);
        }
        return answers;
    }

    public void postQuiz(Quiz quiz) {
        if(quiz == null){
            listener.errorResponse("Quiz was null");
        }
        Log.d(TAG,"Post");

        try {
            JSONObject body = getPostQuizJson(quiz);
            makePostRequest(body);
        } catch (JSONException e) {
            listener.errorResponse(e.getMessage());
        }
    }

    private void makePostRequest(JSONObject body) {
        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, API_URL,
                body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "OnResponse Post");
                listener.postResponse();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.errorResponse(error.getMessage());
            }
        });

        RequestQueueProvider.getInstance(context).addToRequestQueue(postRequest);
    }

    private JSONObject getPostQuizJson(Quiz quiz) throws JSONException {
            JSONObject quizJson = new JSONObject();
            quizJson.put("name", quiz.getName());
            quizJson.put("description", quiz.getDescription());
            quizJson.put("questions", getQuestionsJson(quiz));
            return quizJson;
    }

    private JSONArray getQuestionsJson(Quiz quiz) throws JSONException {
        JSONArray questionsJson = new JSONArray();

        for(Question q : quiz.getQuestions()){
            JSONObject questionJson = new JSONObject();

            questionJson.put("questionText", q.getQuestionText());
            questionJson.put("type", q.getType());
            questionJson.put("video", q.getVideo());
            questionJson.put("answers", getAnswersJson(q));

            questionsJson.put(questionJson);
        }
        return questionsJson;
    }

    private JSONArray getAnswersJson(Question question) throws JSONException {
        JSONArray answersJson = new JSONArray();

        for(Answer answer : question.getAnswers()){
            JSONObject answerJson = new JSONObject();
            answerJson.put("answerText", answer.getAnswerText());
            answerJson.put("correctAnswer", answer.isCorrectAnswer());

            answersJson.put(answerJson);
        }
        return answersJson;
    }
}
