package com.example.jfransen44.recycleit;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FavoritesListActivity extends Activity {

    ListView listView;
    String placeID;
    String userName;
    List<String> placeName = new ArrayList<String>();

    private String[] placesDetail = new String[6];
    HashMap<String, String> favMap;
    HashMap<String, String> favMap2;

    private final String GOOGLE_API_KEY = "AIzaSyC3rGjeJyuj6yno2EpPeRiijYbm1hK7RXQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);

        userName = getIntent().getExtras().getString("userName");

        favMap = (HashMap<String, String>) getIntent().getSerializableExtra("favMap");
        favMap2 = (HashMap<String, String>) favMap.clone();
        final Iterator iterator = favMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry pair = (Map.Entry) iterator.next();
            placeName.add((String) pair.getValue());
            iterator.remove();
        }
        Log.d("FAVAAAAAAAA", favMap.toString());

        for (int i = 0; i < placeName.size(); i++){
            Log.d("PLACENAME", placeName.get(i));
        }

        listView = (ListView) findViewById(R.id.favoritesList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, placeName);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listView.getItemAtPosition(itemPosition);

                Log.d("FAVMAP", favMap2.toString());
                for (Object o : favMap2.keySet()) {
                    Log.d("FORLOP", o.toString());
                    if (favMap2.get(o).equals(itemValue)) {
                        placeID = o.toString();
                    }
                }

                Log.d("PLACEID", "<" + placeID + ">");
                startDetailActivity();
                //Toast.makeText(getApplicationContext(), "Position: " + itemPosition + "ListItem: " + itemValue, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void startDetailActivity(){
        StringBuilder googlePlacesDetailURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlacesDetailURL.append("placeid=" + placeID);
        googlePlacesDetailURL.append("&key=" + GOOGLE_API_KEY);
        FavoritesReadTask favoritesReadTask = new FavoritesReadTask(this);
        Object[] toPass = new Object[1];
        toPass[0] = googlePlacesDetailURL.toString();
        favoritesReadTask.execute(toPass);
        Log.d("DETAIL", googlePlacesDetailURL.toString());
    }

    public void setPlacesDetail(String result){
        Log.d("RESULT", result);
        GooglePlaces googlePlaces = new GooglePlaces();
        this.placesDetail = googlePlaces.parseDetails(result);
        Bundle extras = new Bundle();
        extras.putString("placeID", placeID);
        extras.putStringArray("businessDetails", placesDetail);
        extras.putString("userName", userName);
        Intent intent = new Intent(this, BusinessDetailActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }

}
