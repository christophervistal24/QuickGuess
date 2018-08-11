package com.example.michellebiol.sampleapp.AnswerModule;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.LifeModule.Life;
import com.example.michellebiol.sampleapp.R;
import com.example.michellebiol.sampleapp.databinding.ActivityCategoryQuestionBinding;

public class Check {

    public static String answer;
    private static String correct_answer;
    private static int userPoints = 0;
    public static void correctAnswer(String correctAnswer)
    {
        Check.correct_answer = correctAnswer;
    }



    public static boolean checkAnswer(Activity activity)
    {
        if (Check.answer.equals(Check.correct_answer.trim()))
        {
            int points = Correct.addPoints(++userPoints);
            Toast.makeText(activity.getApplicationContext(),String.valueOf(points),Toast.LENGTH_SHORT).show();
            return true;
        }
        Life life = new Life(activity.getApplicationContext());
        life.setLife(String.valueOf(Wrong.decreaseLife(life.getLife())));
        return false;
    }

}
