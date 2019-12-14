package mprog.project.quizapp.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mprog.project.quizapp.model.Answer;
import mprog.project.quizapp.model.Question;
import mprog.project.quizapp.model.Quiz;

public class QuizLoader {

    // Load static quizzes to MapStorage.
    public static List<Quiz> getQuizzes(){
        Set<Answer> answers = createAnswers();
        Set<Question> questions = createQuestions(answers);
        List<Quiz> quizzes = createQuizzes(questions);
        return quizzes;
    }

    // Creates quizzes.
    private static List<Quiz> createQuizzes(Set<Question> questions) {
        List<Quiz> quizzes = new ArrayList<>();

        Quiz quiz1 = new Quiz();
        quiz1.setName("Q1");
        quiz1.setDescription("This is Q1");
        quiz1.setId(1l);
        quiz1.setQuestions(questions);

        quizzes.add(quiz1);

        Quiz quiz2 = new Quiz();
        quiz2.setName("Q2");
        quiz2.setDescription("This is Q2");
        quiz2.setId(2l);
        quiz2.setQuestions(questions);

        quizzes.add(quiz2);

        return quizzes;
    }

    // Creates questions.
    private static Set<Question> createQuestions(Set<Answer> answers) {
        Set<Question> questions = new HashSet<>();

        Question question1 = new Question();
        question1.setQuestionText("Question1?");
        question1.setType(Question.QuestionType.TEXT);
        question1.setAnswers(answers);

        questions.add(question1);

        Question question2 = new Question();
        question2.setQuestionText("Question2?");
        question2.setType(Question.QuestionType.TEXT);
        question2.setAnswers(answers);

        questions.add(question2);

        return questions;
    }

    // Creates answers.
    private static Set<Answer> createAnswers() {
        Set<Answer> answers = new HashSet<>();

        Answer answer1 = new Answer();
        answer1.setId(1l);
        answer1.setAnswerText("Wrong");
        answer1.setCorrectAnswer(false);

        answers.add(answer1);

        Answer answer2 = new Answer();
        answer2.setId(2l);
        answer2.setAnswerText("Right");
        answer2.setCorrectAnswer(true);

        answers.add(answer2);
        return answers;
    }
}
