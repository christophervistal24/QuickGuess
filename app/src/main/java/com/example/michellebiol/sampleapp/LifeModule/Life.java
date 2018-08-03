package com.example.michellebiol.sampleapp.LifeModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.CategoryQuestion;
import com.example.michellebiol.sampleapp.PointModule.Points;

public class Life {
    private int LIFE = 5;
    private Context context;
    private SharedPreferences sharedPref;
    private String messageResult;
    private Points p;

    public Life(Context context) {
        sharedPref = context.getSharedPreferences("user_life", Context.MODE_PRIVATE);
        this.context = context;
        p = new Points(context);
    }


    public boolean isGameOver(int playersLife)
    {
        CategoryQuestion categoryQuestion;
        return playersLife == 1;
    }

    public String questionResult(String question_result,String life)
    {

        if (!question_result.isEmpty()) {
            messageResult = decrementLife(Integer.parseInt(life));
        }
        return messageResult;
    }

    private String decrementLife(int playersLife)
    {
        if (isGameOver(playersLife)) {
            context.getSharedPreferences("user_life", Context.MODE_PRIVATE);
            SharedPreferences.Editor sEditor = sharedPref.edit();
            sEditor.putString("user_life",String.valueOf(this.LIFE));
            sEditor.apply();
           return "Game over";
       } else {
            playersLife--;
            context.getSharedPreferences("user_life", Context.MODE_PRIVATE);
            SharedPreferences.Editor sEditor = sharedPref.edit();
            sEditor.putString("user_life",String.valueOf(playersLife));
            sEditor.apply();
            return "-1 Life";
        }
    }

    public String setUserLife()
    {
        if (isUserExists()) {
            context.getSharedPreferences("user_life", Context.MODE_PRIVATE);
            return sharedPref.getString("user_life","");
        } else {
            context.getSharedPreferences("user_life", Context.MODE_PRIVATE);
            SharedPreferences.Editor sEditor = sharedPref.edit();
            sEditor.putString("user_life",String.valueOf(this.LIFE));
            sEditor.apply();
            return String.valueOf(this.LIFE);
        }
    }

    private boolean isUserExists()
    {
        String userLife = sharedPref.getString("user_life", "");
        return !userLife.isEmpty();
    }

}
