package com.example.jfransen44.recycleit;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

public class FavoritesReadTask extends AsyncTask<Object, Integer, String> {
    String googlePlacesData = null;
    GoogleMap mMap;
    FavoritesList favoritesList;

    public FavoritesReadTask(FavoritesList favoritesList){
        this.favoritesList = favoritesList;
    }
    @Override
    protected String doInBackground(Object... inputObj) {
        try{
            String googlePlacesURL = (String) inputObj[1];
            GoogleHttp http = new GoogleHttp();
            googlePlacesData = http.readGoogle(googlePlacesURL);
        }
        catch (Exception e){
            Log.d("Google Places Read Task", e.toString());
        }
        return googlePlacesData;
    }

    protected void onPostExecute(String result){
        favoritesList.setFavoritesDetail(result);
    }
}