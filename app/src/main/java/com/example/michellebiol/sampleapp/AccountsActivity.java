package com.example.michellebiol.sampleapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.Adapters.AccountsAdapter;
import com.example.michellebiol.sampleapp.Adapters.QuestionsAdapter;
import com.example.michellebiol.sampleapp.Dialogs.PlayerNameDialog;
import com.example.michellebiol.sampleapp.Interfaces.IAccountsApi;
import com.example.michellebiol.sampleapp.Interfaces.IPhoneInfo;
import com.example.michellebiol.sampleapp.Interfaces.IQuestionByCategoryApi;
import com.example.michellebiol.sampleapp.Models.AccountsItem;
import com.example.michellebiol.sampleapp.Models.AccountsResponse;
import com.example.michellebiol.sampleapp.Models.CategoriesResponse;
import com.example.michellebiol.sampleapp.Models.QuestionsItem;
import com.example.michellebiol.sampleapp.Models.QuestionsResponse;
import com.example.michellebiol.sampleapp.RegisterModule.RegisterUser;
import com.google.zxing.common.StringUtils;


import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountsActivity extends AppCompatActivity implements PlayerNameDialog.PlayerNameDialogListener,IPhoneInfo{
    IAccountsApi service;
    RegisterUser registerObject;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<AccountsItem> accountsItems;
    Button addAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        registerObject  = new RegisterUser(this);
        //check if the phone_id from database and the current_db is equal
        getPhoneAccounts();
        addAccount = (Button) findViewById(R.id.addAccount);
        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInputDialog();
            }
        });
    }


    @Override
    public String[] getPhoneInfo() {
        TelephonyManager telMan = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return new String[]{Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID) , wm.getConnectionInfo().getMacAddress()};
    }

    private void getPhoneAccounts()
    {
        SharedPreferences sharedPref = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        String token_type = sharedPref.getString("token_type","");
        String token = sharedPref.getString("token","");
        accountsItems = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IAccountsApi.class);

        Call<List<AccountsResponse>> call = service.getAccounts(token_type+token,getPhoneInfo()[0]);
        call.enqueue(new Callback<List<AccountsResponse>>() {
            @Override
            public void onResponse(Call<List<AccountsResponse>> call, Response<List<AccountsResponse>> response) {
                List<AccountsResponse> accounts = response.body();
                for(AccountsResponse a : accounts)
                {

                    AccountsItem accountsItem = new AccountsItem(
                            a.getUser().getUsername() , a.getUser().getId()

                    );

                    accountsItems.add(accountsItem);
                }

                adapter = new AccountsAdapter(accountsItems,getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<AccountsResponse>> call, Throwable t) {
                Toast.makeText(AccountsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public static String upperCaseFirst(String value) {

        // Convert String to char array.
        char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);
        // Return string.
        return new String(array);
    }

    public void openInputDialog()
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PlayerNameDialog playerNameDialog = new PlayerNameDialog();
        playerNameDialog.show(ft,"Example Dialog");
    }

    @Override
    public void applyText(String playerName, String question, String questionAnswer) {
        if (HomeActivity.instance.isFieldsAreEmpty(playerName,question,questionAnswer))
        {
            openInputDialog();
        } else {
            String[] p_info = getPhoneInfo();
            String[] user_credentials = {playerName,playerName,question,questionAnswer};
            registerObject.setCredentials(p_info,user_credentials);
        }
    }
}
