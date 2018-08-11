package com.example.michellebiol.sampleapp.AnswerModule;

import android.util.Log;

public class Result {

    public static String questionResult(boolean result)
    {
        if (result)
                return "Correct";
        else
                return "Wrong";
    }
}
