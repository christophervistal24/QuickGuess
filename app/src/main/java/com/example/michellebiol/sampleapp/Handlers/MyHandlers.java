package com.example.michellebiol.sampleapp.Handlers;

import android.app.Activity;
import android.os.SystemClock;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.michellebiol.sampleapp.AnswerModule.Check;
import com.example.michellebiol.sampleapp.AnswerModule.Correct;
import com.example.michellebiol.sampleapp.LifeModule.Life;

public class MyHandlers {

    private static long mLastClickTime = 0;


    public static void avoidMultipleClick()
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
         mLastClickTime = SystemClock.elapsedRealtime();
    }

}
