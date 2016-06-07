package com.example.jfransen44.recycleit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityStatusString(context);
        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if(status==NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
                //new NoInternet(context).execute();
                //intent = new Intent(NetworkChangeReceiver.this, NoInternet.class);
                //startActivity(intent);
                Log.d(">>>>>>>>>>>>>>", "network DOWN!");
                //context.startActivity(new Intent(context, NoInternet.class));

                Intent i = new Intent(context, NoInternet.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }else{
                //new ResumeForceExitPause(context).execute();
                Log.d(">>>>>>>>>>>>>>", "network UPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP!");
                //context.startActivity(new Intent(context, MainActivity.class));
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }

        }
    }
}