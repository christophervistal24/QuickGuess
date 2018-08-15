package com.example.michellebiol.sampleapp.AnswerModule;


import android.app.Activity;
import com.example.michellebiol.sampleapp.LifeModule.Life;
import com.example.michellebiol.sampleapp.PointModule.Points;

public class Check {

    public static String answer;
    private static String correct_answer;
    public static int userPoints = 0;
    public static int trackedPoints;

    public static void correctAnswer(String correctAnswer)
    {
        Check.correct_answer = correctAnswer;
    }

    public static boolean checkAnswer(Activity activity)
    {
        Life life = new Life(activity.getApplicationContext());
//        Points point = new Points(activity.getApplicationContext());
        if (Check.answer.equals(Check.correct_answer.trim()))
        {
            trackedPoints = Correct.addPoints(++userPoints);
            return true;
        }
        life.setLife(String.valueOf(Wrong.decreaseLife(life.getLife())));
        return false;
    }

}
