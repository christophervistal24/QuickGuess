package com.example.michellebiol.sampleapp.TimerModule;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.michellebiol.sampleapp.BR;

public class CountDown extends BaseObservable{

    public String timer;

    public CountDown(long timer) {
        this.setTimer(String.valueOf(timer));
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

}
