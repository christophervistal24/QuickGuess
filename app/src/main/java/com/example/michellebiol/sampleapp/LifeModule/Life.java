package com.example.michellebiol.sampleapp.LifeModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.CategoryQuestion;
import com.example.michellebiol.sampleapp.Helpers.SharedPreferenceHelper;
import com.example.michellebiol.sampleapp.PointModule.Points;
import com.facebook.share.Share;

public class Life {
    private int LIFE = 5;
    private Context context;
    private SharedPreferences sharedPref;
    private String messageResult;
    private Points p;

    public Life(Context context) {
        SharedPreferenceHelper.PREF_FILE="user_life";
        this.context = context;
        p = new Points(context);
    }


    private boolean isGameOver(int playersLife)
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
            SharedPreferenceHelper.setSharedPreferenceInt(context,"user_life",this.LIFE);
           return "Game over";
       } else {
            playersLife--;
            SharedPreferenceHelper.setSharedPreferenceInt(context,"user_life",playersLife);
            return "-1 Life";
        }
    }

    public int setUserLife()
    {
        if (isUserExists()) {
            return SharedPreferenceHelper.getSharedPreferenceInt(context,"user_life",0);
        } else {
            SharedPreferenceHelper.setSharedPreferenceInt(context,"user_life",this.LIFE);
            return this.LIFE;
        }
    }

    private boolean isUserExists()
    {
        int userLife = SharedPreferenceHelper.getSharedPreferenceInt(context,"user_life",0);
        return userLife != 0;
    }

}
