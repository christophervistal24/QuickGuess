package com.example.michellebiol.sampleapp.LifeModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.example.michellebiol.sampleapp.AnswerModule.Check;
import com.example.michellebiol.sampleapp.Helpers.SharedPreferenceHelper;
import com.example.michellebiol.sampleapp.PointModule.Points;
import com.example.michellebiol.sampleapp.R;

public class Life extends BaseObservable {
    public static String life = "5";
    private Context context;
    private SharedPreferences sharedPreferences;
    private Points p;

    public Life(Context context) {
        SharedPreferenceHelper.PREF_FILE = "tokens";
        String user_id = SharedPreferenceHelper.getSharedPreferenceString(context,"user_id",null);
        sharedPreferences = context.getSharedPreferences(user_id+"_life", Context.MODE_PRIVATE);
        this.context = context;
        p = new Points(context);
    }


    @Bindable
    public String getLife() {
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putInt("life",Integer.parseInt(life));
       editor.apply();
       return life;
    }


    private void isGameOver()
    {
       Life.life = "5";
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putInt("life",5);
       editor.apply();
       p.insertPoints(Check.trackedPoints);
       Check.userPoints = 0;
    }

    public void setLife(String life) {
        if(Life.life.equals("0") || Life.life == null) {
            isGameOver();
        } else {
            Life.life = life;
        }
        notifyPropertyChanged(BR.life);
    }
}
