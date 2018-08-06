package com.example.michellebiol.sampleapp.Helpers;

import com.example.michellebiol.sampleapp.Models.QuestionsItem;

import java.util.List;
import java.util.Random;

public class RandomizeHelper {

    //randomize the questions
    public static List<QuestionsItem> questions(List<QuestionsItem> arr, int n)
    {
        Random r = new Random();

        for(int i = n-1; i > 0; i--)
        {
            int j = r.nextInt(i);

            QuestionsItem temp = arr.get(i);
            arr.set(i, arr.get(j));
            arr.set(j, temp);
        }

        return arr;
    }

    //randomize the choices
    public static String[] choices(String arr[] , int n)
    {
        Random r = new Random();

        for(int i = n-1; i > 0; i--)
        {
            int j = r.nextInt(i);

            String temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }

        return arr;
    }

}
