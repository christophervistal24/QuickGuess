package com.example.michellebiol.sampleapp.LifeModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.example.michellebiol.sampleapp.Helpers.SharedPreferenceHelper;
import com.example.michellebiol.sampleapp.R;

public class Life extends BaseObservable {
    public static String life = "5";
    private Context context;
    private SharedPreferences sharedPreferences;

    public Life(Context context) {
        sharedPreferences = context.getSharedPreferences("user_life", Context.MODE_PRIVATE);
        this.context = context;
    }


    @Bindable
    public String getLife() {
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putInt("life",Integer.parseInt(life));
       editor.apply();
       return life;
    }

    public void setLife(String life) {
        Life.life = life;
        notifyPropertyChanged(BR.life);
    }
}
