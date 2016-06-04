package com.example.jfransen44.recycleit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FavoritesListActivity extends Activity {

    ListView listView;

    private final String GOOGLE_API_KEY = "AIzaSyC3rGjeJyuj6yno2EpPeRiijYbm1hK7RXQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);
        String[] favList = getIntent().getStringArrayExtra("favList");

        listView = (ListView) findViewById(R.id.favoritesList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, favList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listView.getItemAtPosition(itemPosition);

                Toast.makeText(getApplicationContext(), "Position: " + itemPosition + "ListItem: " + itemValue, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*ublic void setNames (String result){
        StringBuilder googlePlacesDetailURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlacesDetailURL.append("placeid=" + placeID);
        googlePlacesDetailURL.append("&key=" + GOOGLE_API_KEY);
        PlacesDetailReadTask placesDetailReadTask = new PlacesDetailReadTask(this);
        Object[] toPass = new Object[1];
        toPass[0] = googlePlacesDetailURL.toString();
        placesDetailReadTask.execute(toPass);
    }*/
}
