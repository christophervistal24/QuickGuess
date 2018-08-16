package com.example.michellebiol.sampleapp.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.AccountsActivity;
import com.example.michellebiol.sampleapp.CategoriesActivity;
import com.example.michellebiol.sampleapp.Interfaces.IRegisterQuestionApi;
import com.example.michellebiol.sampleapp.Interfaces.IRegisterUserApi;
import com.example.michellebiol.sampleapp.Models.RegisterQuestionResponse;
import com.example.michellebiol.sampleapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlayerNameDialog extends AppCompatDialogFragment{

    private EditText editUsername;
    private EditText editQuestionAnswer;
    private PlayerNameDialogListener listener;
    private String question;
    IRegisterQuestionApi services;
    ArrayList<String> questionList;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //initialize get request for API
        getRegisterQuestions();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);


        final Spinner spinnerQuestion = (Spinner) view.findViewById(R.id.spinnerQuestions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,questionList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editQuestionAnswer = view.findViewById(R.id.editQuestionAnswer);
        spinnerQuestion.setAdapter(adapter);

        builder.setView(view)
                .setTitle("Create an account")
                .setNegativeButton("Access my accounts", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent  = new Intent(getContext(),AccountsActivity.class);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String playerName = editUsername.getText().toString();
                        String playerAnswer = editQuestionAnswer.getText().toString();
                        if (!spinnerQuestion.getSelectedItem().toString().equalsIgnoreCase("Please choose a question…"))
                        {
                             question = spinnerQuestion.getSelectedItem().toString();

                        }
                        listener.applyText(playerName,question,playerAnswer);
                    }
                });
        editUsername = view.findViewById(R.id.editUsername);
        return builder.create();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }

    private void getRegisterQuestions()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        services = retrofit.create(IRegisterQuestionApi.class);

        questionList = new ArrayList<>();
        questionList.add("Please choose a question…");

        Call<List<RegisterQuestionResponse>> registerQuestionResponseCall = services.getQuestions();
        registerQuestionResponseCall.enqueue(new Callback<List<RegisterQuestionResponse>>() {
            @Override
            public void onResponse(Call<List<RegisterQuestionResponse>> call, Response<List<RegisterQuestionResponse>> response) {
                List<RegisterQuestionResponse> questions = response.body();

                if (questions != null && response.isSuccessful()) {
                    for(RegisterQuestionResponse q : questions)
                    {
                       questionList.add(q.getQuestion());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RegisterQuestionResponse>> call, Throwable t) {

            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PlayerNameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement PlayernameDialogListener");
        }

    }


    public interface PlayerNameDialogListener{
        void applyText(String playerName, String question , String questionAnswer);
    }
}
