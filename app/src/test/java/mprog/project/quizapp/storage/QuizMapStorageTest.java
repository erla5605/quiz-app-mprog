package mprog.project.quizapp.storage;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class QuizMapStorageTest {

    QuizMapStorage quizMap;

    @Before
    public void setup(){
        quizMap = QuizMapStorage.getInstance();
    }

    @Test
    public void testDataLoader(){
        assertEquals(2, quizMap.getQuizzes().size());
    }

    @Test
    public void getQuizTest(){
        assertNotNull(quizMap.getQuiz(1L));
    }

    @Test
    public void getQuiz1Test(){
        assertEquals("Q1", quizMap.getQuiz(1L).getName());
    }

    @Test
    public void deleteNonExistingQuizTest(){
        assertNull(quizMap.delete(null));
    }
}
