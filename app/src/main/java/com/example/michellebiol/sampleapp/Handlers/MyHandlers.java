package com.example.michellebiol.sampleapp.Handlers;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.michellebiol.sampleapp.CategoryQuestion;
import com.example.michellebiol.sampleapp.R;

public class MyHandlers{

    public Activity activity;
    private long mLastClickTime = 0;

    public MyHandlers(Activity activity) {
        this.activity = activity;
    }

    public void onCustomCheckChanged(RadioGroup radio, int radioId)  {

        avoidMultipleClick();
        Log.d("Value" , getSelectedAnswer(radioId) );
    }

    private void avoidMultipleClick()
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
         mLastClickTime = SystemClock.elapsedRealtime();
    }

    private String getSelectedAnswer(int checkedRadioId)
    {
        RadioButton selectedRadioValue = activity.findViewById(checkedRadioId);
        return (String) selectedRadioValue.getText();
    }

}
