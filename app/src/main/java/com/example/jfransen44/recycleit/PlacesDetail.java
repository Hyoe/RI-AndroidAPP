package com.example.jfransen44.recycleit;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Spock on 5/18/2016.
 */
public class PlacesDetail extends AsyncTask<Object, Integer, String> {
    String googlePlacesData = null;
    GoogleMap mMap;
    MainActivity mainActivity;

    public PlacesDetail(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(Object... inputObj) {
        try{
            mMap = (GoogleMap) inputObj[0];
            String googlePlacesURL = (String) inputObj[1];
            GoogleHttp http = new GoogleHttp();
            googlePlacesData = http.readGoogle(googlePlacesURL);
            Log.d("GOOGLEPLACESDATARETURN", googlePlacesData.toString());
        }
        catch (Exception e){
            Log.d("Google Places Read Task", e.toString());
        }
        return googlePlacesData;
    }

    protected void onPostExecute(String result){
        //Log.d("Tag", result);
        mainActivity.asyncResult2(result);

    }
}
