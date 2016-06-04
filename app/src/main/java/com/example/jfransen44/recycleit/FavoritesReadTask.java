package com.example.jfransen44.recycleit;

import android.os.AsyncTask;
import android.util.Log;

public class FavoritesReadTask extends AsyncTask<Object, Integer, String> {
    String googlePlacesData = null;
    FavoritesListActivity favoritesListActivity;

    public FavoritesReadTask(FavoritesListActivity favoritesListActivity){
        this.favoritesListActivity = favoritesListActivity;
    }
    @Override
    protected String doInBackground(Object... inputObj) {
        try{
            String googlePlacesURL = (String) inputObj[0];
            Log.d("URL", googlePlacesURL);
            GoogleHttp http = new GoogleHttp();
            googlePlacesData = http.readGoogle(googlePlacesURL);
        }
        catch (Exception e){
            Log.d("Favorites Read Task", e.toString());
        }
        Log.d("GDATA", googlePlacesData);
        return googlePlacesData;
    }

    protected void onPostExecute(String result){
        favoritesListActivity.setPlacesDetail(result);
    }
}