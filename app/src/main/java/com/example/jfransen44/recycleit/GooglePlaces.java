package com.example.jfransen44.recycleit;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JFransen44 on 4/30/16.
 */
//parse data returned from Google Places
public class GooglePlaces {

    public List<HashMap<String, String>> parse(JSONObject jsonObject, int callingCode){
        JSONArray jsonArray = null;

        if (callingCode == 1001) {
            try {
                jsonArray = jsonObject.getJSONArray("results");
                Log.d("GOOGLEPLACES JSONARRAY", jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return getPlaces(jsonArray, callingCode);
        }
        else {
            JSONObject jo = new JSONObject();
            try {
                jo = jsonObject.getJSONObject("result");
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            Log.d("YOURMOM", jo.toString());
            try {
                //String wtf = jo.
                jsonArray = (JSONArray) jo.getJSONArray("result");

                Log.d("GOOGLEPLACES JA DETAIL", jsonArray.toString());
            } catch (JSONException e) {
                //e.printStackTrace();
            }
            jsonArray = jo.names();
            Log.d("JONAMEA", jsonArray.toString());
            return getPlaces(jsonArray, callingCode);
        }
    }

    //get full list of places
    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray, int callingCode){
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> placeMap = null;

        for (int i = 0; i < placesCount; i++){
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i), callingCode);
                placesList.add(placeMap);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return placesList;
    }


    private HashMap<String, String> getPlace(JSONObject googlePlaceJson, int callingCode){
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        if (callingCode == 1001) {
            String placeName = "NA";
            String vicinity = "NA";
            String latitude = "";
            String longitude = "";
            String reference = "";

            try {
                if (!googlePlaceJson.isNull("name")) {
                    placeName = googlePlaceJson.getString("name");
                }
                if (!googlePlaceJson.isNull("vicinity")) {
                    vicinity = googlePlaceJson.getString("vicinity");
                }
                Log.d("Google Place JSON", googlePlaceJson.toString());
                latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = googlePlaceJson.getString("reference");
                googlePlaceMap.put("place_name", placeName);
                googlePlaceMap.put("vicinity", vicinity);
                googlePlaceMap.put("lat", latitude);
                googlePlaceMap.put("lng", longitude);
                googlePlaceMap.put("reference", reference);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return googlePlaceMap;
        }
        else {
            String phoneNumber = "NA";
            String businessHours = "NA";
            String iconURL = "NA";

            try{
                if (!googlePlaceJson.isNull("formatted_phone_number")){
                    phoneNumber = googlePlaceJson.getString("formatted_phone_number");
                }
                if (!googlePlaceJson.isNull("weekday_text")){
                    businessHours = googlePlaceJson.getString("weekday_text");
                }
                if (!googlePlaceJson.isNull("icon")){
                    iconURL = googlePlaceJson.getString("icon");
                }
                googlePlaceMap.put("phoneNumber", phoneNumber);
                googlePlaceMap.put("businessHours", businessHours);
                googlePlaceMap.put("iconURL", iconURL);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            Log.d("CODE 1000 place map" , googlePlaceMap.toString());
            return googlePlaceMap;
        }

    }
}
