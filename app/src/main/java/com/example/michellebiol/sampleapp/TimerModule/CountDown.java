package com.example.michellebiol.sampleapp.TimerModule;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.michellebiol.sampleapp.BR;
import com.example.michellebiol.sampleapp.CategoryQuestion;
import com.example.michellebiol.sampleapp.Interfaces.OnTickListener;
import com.example.michellebiol.sampleapp.Models.CountDownModel;

import java.util.concurrent.TimeUnit;

public class CountDown extends BaseObservable{

    public String timer;

    public CountDown(long timer) {
        this.setTimer(String.valueOf(timer));
    }

    @Bindable
    public String getTimer() {
        return timer;
    }


    public void setTimer(String timer) {
        this.timer = timer;
        notifyPropertyChanged(BR.timer);
    }

}
