package com.example.jfransen44.recycleit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, LocationSource.OnLocationChangedListener {

    private GoogleMap mMap;
    private final LatLng csumbLatLng = new LatLng(36.654458, -121.801567);
    private final String GOOGLE_API_KEY = "AIzaSyC3rGjeJyuj6yno2EpPeRiijYbm1hK7RXQ";
    private final float defaultZoom = (float) 17.0;
    private GoogleApiClient mGoogleApiClient;
    private Button zipSearchButton;
    private EditText zipTextBox;
    private String zipCode;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private String mActivityTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Marker myMarker;
    private LatLng newPlace;
    private String gQueryResult;
    private List<HashMap<String, String>> gPlacesList;
    String session_username = null;
    String session_firstName = null;
    String session_lastName = null;
    private boolean loggedIn = false;
    String[] favArray;
    private String[] loggedInMenu = { "Logout", "Favorites", "Comments", "About" };
    private String[] loggedOutMenu = { "Login", "Register", "About" };
    HashMap<Marker, Integer> markerHashMap;
    List<HashMap<String, String>> placesDetail = null;
    List<HashMap<String, String>> placesMoreDetail = null;
    List<String> businessDetails = new ArrayList<String>();
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //when you return from another actiivty, we check if that activity had a request code, so we can set certain session variables.
        if (requestCode == 222) {
            //returning from register
            if (resultCode == RESULT_OK) {
                loggedIn = true;
                addDrawerItems(loggedInMenu);
                setupDrawer();
                setupDrawerListener();
                Toast.makeText(MainActivity.this, data.toString(), Toast.LENGTH_SHORT).show();
                session_username = data.getStringExtra("username");
                //Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Hi "+ session_username +"!", Toast.LENGTH_SHORT).show();

            }
        }

        if (requestCode == 111) {
            //returning from login

            if (resultCode == RESULT_OK) {
                loggedIn = true;
                addDrawerItems(loggedInMenu);
                setupDrawer();
                setupDrawerListener();
                session_username = data.getStringExtra("username");
                //Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Hello "+ session_username +"!", Toast.LENGTH_SHORT).show();

            }
        }

        if (requestCode == 333) {
            loggedIn = false;
            addDrawerItems(loggedOutMenu);
            setupDrawer();
            setupDrawerListener();
            //returning from log out
            session_username = null;
            session_firstName = null;
            session_lastName = null;
            Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (loggedIn = true && session_username != null){
            addDrawerItems(loggedInMenu);
            setupDrawer();
            setupDrawerListener();
            Toast.makeText(MainActivity.this, "Username and Logged in true", Toast.LENGTH_SHORT).show();
        } else {
            addDrawerItems(loggedOutMenu);
            setupDrawer();
            setupDrawerListener();
        };

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        // TODO this block throws an error
        //show error if google play services unavailable
        // if (! isGooglePlayServicesAvailable()){
        //     finish();
        // }
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used; set up map UI
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                newPlace = place.getLatLng();
            }

            @Override
            public void onError(Status status) {

            }
        });


        // listview for menu
        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // activity title
        mActivityTitle = getTitle().toString();
        // add menu drawer list
        addDrawerItems(loggedOutMenu);
        setupDrawer();
        setupDrawerListener();
// TODO added in merge not cleared yet
        zipSearchButton = (Button) findViewById(R.id.zipSearchButton);
        //zipTextBox = (EditText) findViewById(R.id.zipTextBox);

        //set zipSearchButton listener
        zipSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (newPlace != null) {
                    mMap.clear();

                    getMapInfo(newPlace);


                    mMap.addMarker(new MarkerOptions().position(newPlace).title(zipCode));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPlace, defaultZoom));
                    autocompleteFragment.setText("");
                    newPlace = null;
                }
            }
        });
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker at CSUMB.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        final Location location = locationManager.getLastKnownLocation(bestProvider);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                onLocationChanged(location);
                return false;
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setOnInfoWindowClickListener(this);
        // Add a marker at current location
        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), defaultZoom));


        /*if (location != null){
            onLocationChanged(location);*/

    }

    //TODO: Delete this method; not used
    //convert user entered zip code to lat/lon.  Not used after location services implemented
    public LatLng getLocatonFromZip(View.OnClickListener context, String zipCode){
        Geocoder coder = new Geocoder(this);
        List<android.location.Address> address;
        LatLng userZip = null;

        try{
            address = coder.getFromLocationName(zipCode, 5);
            Log.d("ADDRESS", address.toString());
            if (address == null) {
                return null;
            }
            android.location.Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            Log.d("GETLOCATIONFROMZIP", location.toString());
            userZip = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return userZip;
    }

    //update map icons when map is moved

    public void onLocationChanged(Location location){
        mMap.clear();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions myMarker = new MarkerOptions();
        myMarker.title("Current Location");
        myMarker.position(latLng);
        mMap.addMarker(myMarker);
        getMapInfo(latLng);
    }


    //call google services to place markers on map
    private void getMapInfo(LatLng latLng){
        StringBuilder googlePlacesURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesURL.append("location=" + Double.toString(latLng.latitude) + "," + Double.toString(latLng.longitude));
        googlePlacesURL.append("&radius=" + 8500);
        googlePlacesURL.append("&keyword=recycling|waste_management");
        googlePlacesURL.append("&key=" + GOOGLE_API_KEY);
        Log.d("QueryURLS", googlePlacesURL.toString());
        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask(this);
        Object[] toPass = new Object[2];
        toPass[0] = mMap;
        toPass[1] = googlePlacesURL.toString();
        googlePlacesReadTask.execute(toPass);
        mMap.setOnMarkerClickListener(this);
    }

    // helper method for menu
    private void addDrawerItems(String[] values) {
        // String[] osArray = { "Login", "Favorite", "Comments", "About" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void setupDrawerListener(){

        if(loggedIn == false){
            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            Intent logInIntent = new Intent(MainActivity.this, LoginActivity.class);
                            MainActivity.this.startActivityForResult(logInIntent, 111);
                            //Toast.makeText(MainActivity.this, "Login Pressed", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                            MainActivity.this.startActivityForResult(registerIntent, 222);
                            //Toast.makeText(MainActivity.this, "Register Pressed", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Intent AboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                            MainActivity.this.startActivityForResult(AboutIntent, 444);
                            //Toast.makeText(MainActivity.this, "About Pressed", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                    }
                }

            });
        } else {
            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            Intent intent = new Intent(MainActivity.this, LogOutActivity.class);
                            MainActivity.this.startActivityForResult(intent, 333);
                            //Toast.makeText(MainActivity.this, "Logout Pressed", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            //Intent b = new Intent(MainActivity.this, Activity2.class);
                            //startActivity(b)
                            Toast.makeText(MainActivity.this, "Favorites Pressed", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            //Intent b = new Intent(MainActivity.this, Activity2.class);
                            //startActivity(b);
                            Toast.makeText(MainActivity.this, "Comments Pressed", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Intent AboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                            MainActivity.this.startActivityForResult(AboutIntent, 444);
                            //Toast.makeText(MainActivity.this, "About Pressed", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                    }
                }

            });
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int pos;
        String mTitle = marker.getTitle();
        LatLng mLatLng = marker.getPosition();
        String mPlaceId = null;
        // get key value from results
        for (HashMap<String, String> map : placesDetail)
            for (Map.Entry<String, String> entry : map.entrySet())
                Log.d("KEY_VALUE", entry.getKey() + " => " + entry.getValue());

        // get query result from marker latlng/title
        if(mTitle != null && mLatLng != null) {
            for (int i = 0; i < placesDetail.size(); i++) {
                LatLng businessLatLng = new LatLng(Double.parseDouble(placesDetail.get(i).get("lat")), Double.parseDouble(placesDetail.get(i).get("lng")));
                //Log.d("KEY_VALUE", businessLatLng.toString());
                if (mTitle.equals(placesDetail.get(i).get("place_name")) && mLatLng.equals(businessLatLng)) {
                    //Log.d("KEY_VALUE",placesDetail.get(i).get("place_name") );
                    //Toast.makeText(MainActivity.this,placesDetail.get(i).get("place_name") , Toast.LENGTH_SHORT).show();
                    mPlaceId = placesDetail.get(i).get("reference");

                    // Query for business specific information
                    StringBuilder googlePlacesDetailURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
                    googlePlacesDetailURL.append("reference=" + mPlaceId);
                    googlePlacesDetailURL.append("&key=" + GOOGLE_API_KEY);
                    Log.d("QueryURL", googlePlacesDetailURL.toString());

                    PlacesDetail PlacesDetailTask = new PlacesDetail(this);
                    Object[] toPass = new Object[2];
                    toPass[0] = mMap;
                    toPass[1] = googlePlacesDetailURL.toString();
                    PlacesDetailTask.execute(toPass);

                    // TODO
                    // TODO update marker name and address and add details button
                    // TODO

                    //placeDetails.add();
                    Log.d("placesmoredetial", placesDetail.toString());
                    /*for (HashMap<String, String> map : placesMoreDetail) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            Log.d("MORE_KEY_VALUE", entry.getKey() + " => " + entry.getValue());
                            if(entry.getValue() != null){
                                businessDetails.add(entry.getValue());
                            }
                        }
                    }*/
                    break;

                }
            }
        }
        return false;
    }



    //get results string from GoogleReadTask query
    public void asyncResult(String result){
        if (result != null){
            //Log.d("RESULT", result.toString());
            //gQueryResult = result;
            placesDetail = parseResults(result, 1001);
            //Log.d("GPLACESLIST SIZE", Integer.toString(gPlacesList.size()));
        }
        else
            Log.d("METHOD ASYNCRESULT", "result empty");
    }
    //get results string from GoogleReadTask query
    public void asyncResult2(String result){
        if (result != null){

            placesMoreDetail = parseResults(result, 1000);
            Log.d("MORE_DETAIL", result.toString());
        }
        else
            Log.d("METHOD ASYNCRESULT2", "result empty");
    }


    //parse results from GoogleReadTask query
    private List<HashMap<String, String>> parseResults(String queryResult, int callingCode){
        //JSONObject gPlacesJson;
        List<HashMap<String, String>> parsedResultsList = null;
        GooglePlaces gPlacesParser = new GooglePlaces();

        try{
            JSONObject gPlacesJson = new JSONObject(queryResult);
            parsedResultsList = gPlacesParser.parse(gPlacesJson, callingCode);
            Log.d("PARSEDRESULTSLIST", parsedResultsList.toString());
        }
        catch (Exception e){
            Log.d("PARSERESULTS EXC", e.toString());
        }
        return parsedResultsList;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("oninforwindowclick", "click");
        Intent intent = new Intent(this, BusinessDetailActivity.class);
        intent.putStringArrayListExtra("businessDetail", (ArrayList)businessDetails);
        startActivity(intent);
    }


}
