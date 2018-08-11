package com.example.michellebiol.sampleapp.CategoriesModule;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.michellebiol.sampleapp.Adapters.CategoriesAdapter;
import com.example.michellebiol.sampleapp.Helpers.SharedPreferenceHelper;
import com.example.michellebiol.sampleapp.Interfaces.ICategoriesApi;
import com.example.michellebiol.sampleapp.Models.CategoriesItem;
import com.example.michellebiol.sampleapp.Models.CategoriesResponse;
import com.example.michellebiol.sampleapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryList {

    private Context context;
    private RecyclerView recyclerView;
    private List<CategoriesItem> categoriesItems;
    private String token;
    private String token_type;
    private ICategoriesApi service;

    public CategoryList(final Context context, ICategoriesApi service, RecyclerView viewById) {

        this.context = context;
        this.recyclerView = viewById;
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.categoriesItems = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ICategoriesApi.class);
        getTokens().getCategories();
    }


    private CategoryList getTokens()
    {
        SharedPreferenceHelper.PREF_FILE = "tokens";
        this.token_type = SharedPreferenceHelper.getSharedPreferenceString(context,"token_type",null);
        this.token       = SharedPreferenceHelper.getSharedPreferenceString(context,"token",null);
        return this;
    }

    private void getCategories()
    {
        Call<List<CategoriesResponse>> call = this.service.getCategory(this.token_type.concat(this.token));
        call.enqueue(new Callback<List<CategoriesResponse>>() {
            @Override
            public void onResponse(Call<List<CategoriesResponse>> call, Response<List<CategoriesResponse>> response) {
                    if (response.isSuccessful())
                    {
                        List<CategoriesResponse> categories = response.body();
                        if (categories != null) {
                            insertToRecylerView(categories);
                        }
                    }
            }

            @Override
            public void onFailure(Call<List<CategoriesResponse>> call, Throwable t) {

            }
        });
    }

    private void insertToRecylerView(List<CategoriesResponse> categoriesResponse)
    {
        for(CategoriesResponse c : categoriesResponse)
        {
            CategoriesItem categoriesItem = new CategoriesItem(c.getId() , c.getCategories(), c.getCategories_description());
            this.categoriesItems.add(categoriesItem);
        }
        RecyclerView.Adapter adapter = new CategoriesAdapter(this.categoriesItems, this.context);
        this.recyclerView.setAdapter(adapter);
    }
}
