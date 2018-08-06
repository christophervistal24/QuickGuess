package com.example.michellebiol.sampleapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.CategoriesModule.CategoryList;
import com.example.michellebiol.sampleapp.Helpers.SharedPreferenceHelper;
import com.example.michellebiol.sampleapp.Interfaces.ICategoriesApi;
import com.example.michellebiol.sampleapp.Adapters.CategoriesAdapter;
import com.example.michellebiol.sampleapp.LifeModule.Life;
import com.example.michellebiol.sampleapp.Models.CategoriesItem;
import com.example.michellebiol.sampleapp.Models.CategoriesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CategoriesActivity extends AppCompatActivity {


    ICategoriesApi service;
    CategoryList categoryList;
    protected static CategoriesActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        categoryList = new CategoryList(this , service, (RecyclerView) findViewById(R.id.recyclerView));
    }


}
