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

    public List<HashMap<String, String>> parse(JSONObject jsonObject){
        JSONArray jsonArray = null;


            try {
                jsonArray = jsonObject.getJSONArray("results");
                Log.d("GOOGLEPLACES JSONARRAY", jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return getPlaces(jsonArray);

    }

    //get full list of places
    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray){
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> placeMap = null;

        for (int i = 0; i < placesCount; i++){
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return placesList;
    }


    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();

        String placeName = "NA";
        String vicinity = "NA";
        String latitude = "";
        String longitude = "";
        //String reference = "";
        String placeID = "";

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            //Log.d("Google Place JSON", googlePlaceJson.toString());
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            //reference = googlePlaceJson.getString("reference");
            placeID = googlePlaceJson.getString("place_id");
            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            //=------p[;[googlePlaceMap.put("reference", reference);
            googlePlaceMap.put("placeID", placeID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;
    }

    public String[] parseDetails(String result){
        String[] placeDetails = {"NAME UNAVAILABLE", "ADDRESS UNAVAILABLE", "PHONE UNAVAILABLE", "LOGO UNAVAILABLE", "WEB ADDRESS UNAVAILABLE",
                "HOURS UNAVAILABLE", };
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);
            jsonObject = jsonObject.getJSONObject("result");
            if (! jsonObject.isNull("name")) {
                placeDetails[0] = jsonObject.getString("name");
            }
            if (! jsonObject.isNull("formatted_address")){
                placeDetails[1] = jsonObject.getString("formatted_address");
            }
            if (! jsonObject.isNull("formatted_phone_number")){
                placeDetails[2] = jsonObject.getString("formatted_phone_number");
            }
            if (! jsonObject.isNull("icon")){
                placeDetails[3] = jsonObject.getString("icon");
            }
            if (! jsonObject.isNull("website")){
                placeDetails[4] = jsonObject.getString("website");
            }
            if (! jsonObject.isNull("opening_hours")){
                jsonObject = jsonObject.getJSONObject("opening_hours");
                placeDetails[5] = jsonObject.getString("weekday_text");
                placeDetails[5] = placeDetails[5].substring(1, placeDetails[5].length() - 1);
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return placeDetails;
    }
}
