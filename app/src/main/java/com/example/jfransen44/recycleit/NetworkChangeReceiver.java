package com.example.jfransen44.recycleit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityStatusString(context);
        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if(status==NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
               //Log.d(">>>>>>>>>", "Shutting Down: " + status);
                /*Intent i = new Intent(context, NoInternet.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);*/
            }else{
                //Log.d(">>>>>>>>>", "Starting up inner else");
                /*Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);*/
            }
        }else if (status!=NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
            //Log.d(">>>>>>>>>", "Starting up: " + status);

            /*Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);*/
        }
    }
}