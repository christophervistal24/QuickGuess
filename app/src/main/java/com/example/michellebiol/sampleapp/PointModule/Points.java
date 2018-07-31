package com.example.michellebiol.sampleapp.PointModule;

import android.content.Context;

public class Points {

    public int points = 0;

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points * 100;
    }
}
