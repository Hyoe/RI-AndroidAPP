package com.example.jfransen44.recycleit;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Spock on 6/6/2016.
 */
public class TestInternetConnection extends AppCompatActivity {
    private static TestInternetConnection instance = new TestInternetConnection();
    static Context context;
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

    public static TestInternetConnection getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            //return connected;
            if (!connected) {
                // go to intent
                Log.d("++++++++++", "False!!!!!!!!!!!!!!!!!!!!!");
                //Intent noInternet = new Intent(this, NoInternet.class);
                //startActivity(noInternet);
            }

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

   /* public void testConnection(){
        boolean isInternetPresent = isOnline();
        if (!isInternetPresent) {
            // go to intent
            Intent noInternet = new Intent(TestInternetConnection.this, NoInternet.class);
            startActivity(noInternet);
        }

    }*/
}
