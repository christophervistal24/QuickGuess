package com.example.michellebiol.sampleapp.Detector;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
    Context context;

    public ConnectionDetector(Context context) {
        this.context = context;
    }

    private boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if(info != null)
            {
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkConnection()
    {
        if(this.isConnected())
        {
            return true;
        } else
        {
            return false;
        }
    }


}
