package com.example.jfransen44.recycleit;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JFransen44 on 4/30/16.
 */
//set up connection to Google Play Services
public class GoogleHttp {

    public String readGoogle(String httpURL) throws IOException{
        String googleHttpData = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(httpURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }
            googleHttpData = stringBuffer.toString();
            bufferedReader.close();
        }
        catch (Exception e){
            Log.d("Ex. reading HTTP url", e.toString());
        }
        finally {
            inputStream.close();
            httpURLConnection.disconnect();
        }
        return googleHttpData;
    }
}
