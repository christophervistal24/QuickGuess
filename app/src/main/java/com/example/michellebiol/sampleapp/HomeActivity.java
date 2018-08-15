package com.example.michellebiol.sampleapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.Detector.ConnectionDetector;
import com.example.michellebiol.sampleapp.Dialogs.PlayerNameDialog;
import com.example.michellebiol.sampleapp.Helpers.SharedPreferenceHelper;
import com.example.michellebiol.sampleapp.Interfaces.IPhoneInfo;
import com.example.michellebiol.sampleapp.Interfaces.IRegisterUserApi;
import com.example.michellebiol.sampleapp.LifeModule.Life;
import com.example.michellebiol.sampleapp.RegisterModule.RegisterUser;
import com.example.michellebiol.sampleapp.databinding.ActivityCategoryQuestionBinding;
import com.example.michellebiol.sampleapp.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity implements PlayerNameDialog.PlayerNameDialogListener,IPhoneInfo{

    ConnectionDetector detector;
    RegisterUser registerObject;
    Life life;
    protected static HomeActivity instance;
    ActivityHomeBinding homeBinding;
    SharedPreferences sharedPreferences;
    String lifeOfUser;
    String user_id;
    String life_value;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        instance = this;
        hideNavigationBar();
        life = new Life(this);
        isTokenAlreadySet();
        detector =  new ConnectionDetector(this);
        registerObject  = new RegisterUser(this);
        detector.checkConnection();
       }


    @Override
    protected void onResume()
    {
        if(detector.checkConnection())
        {
            if (sharedPreferences != null){
                life.setLife(String.valueOf(sharedPreferences.getInt("life",5)));
                homeBinding.userLife.setText(String.valueOf(sharedPreferences.getInt("life",5)));
            } else {
                life.setLife(Life.life);
                homeBinding.userLife.setText(Life.life);
            }
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

    public void gotoActivity(View v)
    {   Intent intent;
        switch(v.getId())
        {
            case R.id.btnCategory :
                intent  = new Intent(this,CategoriesActivity.class);
                startActivity(intent);
                break;

            case R.id.btnRanks :
                intent  = new Intent(this,RankingsActivity.class);
                startActivity(intent);
                break;

            case R.id.btnAccounts :
                intent = new Intent(this,AccountsActivity.class);
                startActivity(intent);
                break;
            default :
                Toast.makeText(this,"Hello , World",Toast.LENGTH_SHORT).show();
        }
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
        user_id = SharedPreferenceHelper.getSharedPreferenceString(this,"user_id",null);
        if (token == null && user_id == null)
        {
            openInputDialog();
        } else {
            sharedPreferences = getSharedPreferences(user_id+"_life",0);
            life_value = String.valueOf(sharedPreferences.getInt("life",5));
            lifeOfUser = life_value;
            Life.life = lifeOfUser;
            homeBinding.userLife.setText(lifeOfUser);
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
