package mprog.project.quizapp.api;

import android.content.Context;

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

    // Interface for the listener to the response of the QuizApi.
    public interface QuizApiResponseListener {
        void errorResponse(String error);

        void quizListResponse(List<Quiz> quizzes);

        void quizResponse(Quiz quiz);

        void postResponse();
    }

    private static final String API_URL = "http://10.0.2.2:8080/quizzes";

    private QuizApiResponseListener listener;
    private Context context;

    // Constructor for the QuizApi takes a listener and a context.
    public QuizApi(QuizApiResponseListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    // Makes a request for all the quizzes.
    public void getQuizzes() {
        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, API_URL, null,
                new Response.Listener<JSONArray>() {
                    // Handles the response, creates quizzes from the JSONArray response.
                    // Passes them to the listener.
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
            // Handles if response returns an error, passes the error message to the listener.
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.errorResponse(error.getMessage());
            }
        });

        RequestQueueProvider.getInstance(context).addToRequestQueue(getRequest);
    }

    // Makes a request for the quiz with that id.
    public void getQuiz(UUID id) {
        String url = API_URL + "/" + id;

        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            // Creates a quiz from the JSONObject response, and passes it to the listener.
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Quiz quiz = createQuizFromJson(response);
                    listener.quizResponse(quiz);
                } catch (JSONException e) {
                    listener.errorResponse(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            // Handles if response returns an error, passes the error message to the listener.
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.errorResponse(error.getMessage());
            }
        });
        RequestQueueProvider.getInstance(context).addToRequestQueue(getRequest);
    }

    // Creates a quiz from a JSON object.
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

    // Creates a list of questions from a JSONArray in a JSONObject that represent a quiz.
    private ArrayList<Question> getQuestions(JSONObject quiz) throws JSONException {
        ArrayList<Question> questions = new ArrayList<>();
        JSONArray questionsJson = quiz.getJSONArray("questions");
        if (questionsJson != null) {
            for (int i = 0; i < questionsJson.length(); i++) {
                JSONObject questionJson = questionsJson.getJSONObject(i);
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
        }

        return questions;
    }

    // Creates a list of answers from a JSONArray in a JSONObject that represent a question.
    private List<Answer> getAnswers(JSONObject question) throws JSONException {
        List<Answer> answers = new ArrayList<>();

        JSONArray answersJson = question.getJSONArray("answers");

        if (answersJson != null) {
            for (int i = 0; i < answersJson.length(); i++) {
                JSONObject answerJson = answersJson.getJSONObject(i);

                String answerId = answerJson.getString("id");
                String answerText = answerJson.getString("answerText");
                boolean correctAnswer = answerJson.getBoolean("correctAnswer");

                Answer answer = new Answer();
                answer.setId(UUID.fromString(answerId));
                answer.setAnswerText(answerText);
                answer.setCorrectAnswer(correctAnswer);

                answers.add(answer);
            }
        }
        return answers;
    }

    // Makes a a JSONObject of a quiz and then makes a request to post that quiz.
    public void postQuiz(Quiz quiz) {
        if (quiz == null) {
            listener.errorResponse("Quiz was null");
        }

        try {
            JSONObject body = getPostQuizJson(quiz);
            makePostRequest(body);
        } catch (JSONException e) {
            listener.errorResponse(e.getMessage());
        }
    }

    // Makes a request to post the quiz that was created.
    private void makePostRequest(JSONObject body) {
        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, API_URL,
                body, new Response.Listener<JSONObject>() {
            // Handles the response by calling on the listener.
            @Override
            public void onResponse(JSONObject response) {
                listener.postResponse();
            }
        }, new Response.ErrorListener() {
            // Handles if response returns an error, passes the error message to the listener.
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.errorResponse(error.getMessage());
            }
        });

        RequestQueueProvider.getInstance(context).addToRequestQueue(postRequest);
    }

    // Creates JSONObject from a quiz.
    private JSONObject getPostQuizJson(Quiz quiz) throws JSONException {
        JSONObject quizJson = new JSONObject();
        quizJson.put("name", quiz.getName());
        quizJson.put("description", quiz.getDescription());
        quizJson.put("questions", getQuestionsJson(quiz));
        return quizJson;
    }

    // Creates JSONArray of questions from a quiz.
    private JSONArray getQuestionsJson(Quiz quiz) throws JSONException {
        JSONArray questionsJson = new JSONArray();

        for (Question q : quiz.getQuestions()) {
            JSONObject questionJson = new JSONObject();

            questionJson.put("questionText", q.getQuestionText());
            questionJson.put("type", q.getType());
            questionJson.put("video", q.getVideo());
            questionJson.put("answers", getAnswersJson(q));

            questionsJson.put(questionJson);
        }
        return questionsJson;
    }

    // Creates JSONArray of answers from a question.
    private JSONArray getAnswersJson(Question question) throws JSONException {
        JSONArray answersJson = new JSONArray();

        for (Answer answer : question.getAnswers()) {
            JSONObject answerJson = new JSONObject();
            answerJson.put("answerText", answer.getAnswerText());
            answerJson.put("correctAnswer", answer.isCorrectAnswer());

            answersJson.put(answerJson);
        }
        return answersJson;
    }
}
