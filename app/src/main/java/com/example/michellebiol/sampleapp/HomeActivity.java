package com.example.michellebiol.sampleapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.Detector.ConnectionDetector;
import com.example.michellebiol.sampleapp.Dialogs.PlayerNameDialog;
import com.example.michellebiol.sampleapp.Helpers.SharedPreferenceHelper;
import com.example.michellebiol.sampleapp.Interfaces.IPhoneInfo;
import com.example.michellebiol.sampleapp.Interfaces.IRegisterUserApi;
import com.example.michellebiol.sampleapp.LifeModule.Life;
import com.example.michellebiol.sampleapp.Models.RegisterUserRequest;
import com.example.michellebiol.sampleapp.Models.RegisterUserResponse;
import com.example.michellebiol.sampleapp.RegisterModule.RegisterUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements PlayerNameDialog.PlayerNameDialogListener,IPhoneInfo{

    ImageButton btnCategory;
    TextView userLife;
    IRegisterUserApi services;
    ConnectionDetector detector;
    RegisterUser registerObject;
    Life life;
    protected static HomeActivity instance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        instance = this;
        hideNavigationBar();
        castingElements();
        isTokenAlreadySet();
        classNewInstances();
        detector.checkConnection();
        userLife.setText(String.format("Your life : %s", String.valueOf(life.setUserLife())));
    }

    private void classNewInstances()
    {
        life = new Life(this);
        detector =  new ConnectionDetector(this);
        registerObject  = new RegisterUser(this);
    }

    private void castingElements()
    {
        btnCategory = (ImageButton) findViewById(R.id.btnCategory);
        userLife    = (TextView) findViewById(R.id.userLife);
    }

    @Override
    protected void onResume()
    {
        if(detector.checkConnection())
        {
            Toast.makeText(this, "User is connected", Toast.LENGTH_SHORT).show();
            userLife.setText(String.format("Your life : %s", String.valueOf(life.setUserLife())));
        } else
        {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
        super.onResume();
        hideNavigationBar();

    }

    private void hideNavigationBar() {
        this.getWindow().getDecorView()
        .setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }


    public void categories(View view)
    {
        Intent intent  = new Intent(this,CategoriesActivity.class);
        startActivity(intent);

    }

    public void accounts(View view)
    {
        Intent intent  = new Intent(this,AccountsActivity.class);
        startActivity(intent);

    }

    public void rankings(View view)
    {
        Intent intent  = new Intent(this,RankingsActivity.class);
        startActivity(intent);
    }

    public void openInputDialog()
    {
        PlayerNameDialog playerNameDialog = new PlayerNameDialog();
        playerNameDialog.setCancelable(false);
        playerNameDialog.show(getSupportFragmentManager(),"Example Dialog");
    }

    public static HomeActivity activityInstance()
    {
        return instance;
    }

    @Override
    public void applyText(String playerName,String question , String questionAnswer) {
        if (isFieldsAreEmpty(playerName,question,questionAnswer))
        {
            openInputDialog();
        } else {
            String[] p_info = getPhoneInfo();
            String[] user_credentials = {playerName,playerName,question,questionAnswer};
            registerObject.setCredentials(p_info,user_credentials);
        }
    }

    private boolean isFieldsAreEmpty(String txtPlayerName , String txtQuestion, String txtQuestionAnswer)
    {
        return txtPlayerName.isEmpty() || txtQuestion.isEmpty() || txtQuestionAnswer.isEmpty();
    }

    public void setToken(String[] value)
    {
        SharedPreferenceHelper.PREF_FILE = "tokens";
        SharedPreferenceHelper.setSharedPreferenceString(this,"token",value[0]);
        SharedPreferenceHelper.setSharedPreferenceString(this,"token_type",value[1]);
        SharedPreferenceHelper.setSharedPreferenceString(this,"user_id",value[2]);
    }

    private void isTokenAlreadySet()
    {
        SharedPreferenceHelper.PREF_FILE = "tokens";
        String token = SharedPreferenceHelper.getSharedPreferenceString(this,"token",null);
        if (token == null)
        {
            openInputDialog();
        }
    }

    @Override
    public String[] getPhoneInfo()
    {
        TelephonyManager telMan = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return new String[]{Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID) , wm.getConnectionInfo().getMacAddress()};
    }
}
