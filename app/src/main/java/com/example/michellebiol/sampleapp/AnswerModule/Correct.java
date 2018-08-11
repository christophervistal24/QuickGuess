package com.example.michellebiol.sampleapp.AnswerModule;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.PointModule.Points;

public class Correct{


    public static int addPoints(int userPoints)
    {
        Points points = new Points(userPoints);
        return points.getPoints();
    }
}
