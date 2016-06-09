package com.example.jfransen44.recycleit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, LocationSource.OnLocationChangedListener {

    private GoogleMap mMap;
    private final LatLng csumbLatLng = new LatLng(36.654458, -121.801567);
    private final String GOOGLE_API_KEY = "AIzaSyC3rGjeJyuj6yno2EpPeRiijYbm1hK7RXQ";
    private final float defaultZoom = (float) 17.0;
    private GoogleApiClient mGoogleApiClient;
    private Button zipSearchButton;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private String mActivityTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private LatLng newPlace;
    private LatLng currentSearchLocation;
    private String[] placesDetail = new String[6];
    static String session_username = null;
    static String place_id = null;
    String session_firstName = null;
    String session_lastName = null;
    private boolean loggedIn = false;
    static String[] favPlaceIDList = null;
    static String[] favPlaceNameList = null;
    static HashMap<String, String> favMap = null;
    private String[] loggedInMenu = { "Logout", "Favorites", "Facts", "Recycling Guide", "Why Recycle?", "About" };
    private String[] loggedOutMenu = { "Login", "Register", "Facts", "Recycling Guide", "Why Recycle?", "About" };
    List<HashMap<String, String>> placesMoreDetail = null;
    List<String> businessDetails = new ArrayList<String>();
    private String placeID = "";
    //TestInternetConnection connection = new TestInternetConnection();
    //TestInternetConnection.getInstance(this).testConnection();
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
                favPlaceIDList = LoginActivity.favPlaceIDList.clone();
                favPlaceNameList = LoginActivity.favPlaceNameList;
                favMap = LoginActivity.favMap;
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
                Toast.makeText(MainActivity.this, "Hello "+ session_username +"!", Toast.LENGTH_SHORT).show();
                //SEM added to fix favorites
                favPlaceIDList = LoginActivity.favPlaceIDList.clone();
                favPlaceNameList = LoginActivity.favPlaceNameList;
                favMap = LoginActivity.favMap;
                //SEM
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


        // get Internet status
        //connection.testConnection();


        if (loggedIn = true && session_username != null){
            addDrawerItems(loggedInMenu);
            setupDrawer();
            setupDrawerListener();

        } else {
            addDrawerItems(loggedOutMenu);
            setupDrawer();
            setupDrawerListener();
        };

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // get Internet status
        //connection.();

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

        zipSearchButton = (Button) findViewById(R.id.zipSearchButton);

        //set zipSearchButton listener
        zipSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (newPlace != null) {
                    mMap.clear();
                    getMapInfo(newPlace);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPlace, defaultZoom));
                    autocompleteFragment.setText("");
                    newPlace = null;
                }
            }
        });
    }

    //TODO this is not currently used
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

        //connection.testConnection();

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
        if(location != null) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), defaultZoom));
        }
    }

    //update map icons when map is moved

    public void onLocationChanged(Location location){
        //connection.testConnection();

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
        //connection.testConnection();

        currentSearchLocation = latLng;
        StringBuilder googlePlacesURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesURL.append("location=" + Double.toString(latLng.latitude) + "," + Double.toString(latLng.longitude));
        googlePlacesURL.append("&radius=" + 8500);
        googlePlacesURL.append("&keyword=recycle|recycle_center");
        googlePlacesURL.append("&key=" + GOOGLE_API_KEY);
        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
        Object[] toPass = new Object[2];
        toPass[0] = mMap;
        toPass[1] = googlePlacesURL.toString();
        googlePlacesReadTask.execute(toPass);
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
                            //connection.testConnection();
                            Intent logInIntent = new Intent(MainActivity.this, LoginActivity.class);
                            MainActivity.this.startActivityForResult(logInIntent, 111);
                            //Toast.makeText(MainActivity.this, "Login Pressed", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            //connection.testConnection();
                            Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                            MainActivity.this.startActivityForResult(registerIntent, 222);
                            //Toast.makeText(MainActivity.this, "Register Pressed", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            //connection.testConnection();
                            Intent AboutIntent = new Intent(MainActivity.this, Facts.class);
                            MainActivity.this.startActivityForResult(AboutIntent, 444);
                            //Toast.makeText(MainActivity.this, "About Pressed", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:

                            Intent recycleGuide = new Intent(MainActivity.this, RecycleGuide.class);
                            startActivity(recycleGuide);
                            break;

                        case 4:
                            // Why Recycle
                            Intent whyRecycle = new Intent(MainActivity.this, WhyRecycle.class);
                            startActivity(whyRecycle);
                            break;
                        case 5:
                            // Why Recycle
                            Intent aboutPage = new Intent(MainActivity.this, AboutPage.class);
                            startActivity(aboutPage);
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
                            //connection.testConnection();
                            Intent intent = new Intent(MainActivity.this, LogOutActivity.class);
                            MainActivity.this.startActivityForResult(intent, 333);
                            mMap.clear();
                            favPlaceIDList = null;
                            //Toast.makeText(MainActivity.this, "Logout Pressed", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            //connection.testConnection();
                            //Intent b = new Intent(MainActivity.this, Activity2.class);
                            //startActivity(b)
                            //Toast.makeText(MainActivity.this, "Favorites Pressed", Toast.LENGTH_SHORT).show();

                            favPlaceIDList = LoginActivity.favPlaceIDList.clone();
                            favPlaceNameList = LoginActivity.favPlaceNameList;
                            favMap = LoginActivity.favMap;
                            //TODO ADD INTENT
                            Intent i = new Intent(MainActivity.this, FavoritesListActivity.class);
                            Bundle extras = new Bundle();
                            extras.putSerializable("favMap", favMap);
                            extras.putString("userName", session_username);
                            i.putExtras(extras);
                            startActivity(i);


                            break;
                       /* case 2:
                            //Intent b = new Intent(MainActivity.this, Activity2.class);
                            //startActivity(b);
                            Toast.makeText(MainActivity.this, "Comments Pressed", Toast.LENGTH_SHORT).show();
                            break;*/
                        case 2:
                            //connection.testConnection();

                            Intent AboutIntent = new Intent(MainActivity.this, Facts.class);
                            MainActivity.this.startActivityForResult(AboutIntent, 444);
                            //Toast.makeText(MainActivity.this, "About Pressed", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Intent recycleGuide = new Intent(MainActivity.this, RecycleGuide.class);
                            startActivity(recycleGuide);
                            break;

                        case 4:
                            // Why Recycle
                            Intent whyRecycle = new Intent(MainActivity.this, WhyRecycle.class);
                            startActivity(whyRecycle);
                            break;
                        case 5:
                            // Why Recycle
                            Intent aboutPage = new Intent(MainActivity.this, AboutPage.class);
                            startActivity(aboutPage);
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

   @Override
   //grab place reference for detail query, then delete to hide from user
   public boolean onMarkerClick(Marker marker) {

        placeID = marker.getSnippet();
        place_id = placeID; //for business detail page
        marker.setSnippet("");
       if (favPlaceIDList != null) {
           for (int i = 0; i < favPlaceIDList.length; i++) {
               if (favPlaceIDList[i].equals(placeID)) {
                   marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
               }
           }
       }
        return false;
    }

    //get results string from PlacesDetailReadTask query
    //TODO query DB for accepted materials for this location
    public void setPlacesDetail(String result){

        GooglePlaces googlePlaces = new GooglePlaces();
        this.placesDetail = googlePlaces.parseDetails(result);
        Bundle extras = new Bundle();
        extras.putStringArray("businessDetails", placesDetail);
        extras.putString("userName", session_username);
        extras.putString("placeID", placeID);
        //connection.testConnection();

        Intent intent = new Intent(this, BusinessDetailActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

        // Query for business specific information
            //connection.testConnection();

        StringBuilder googlePlacesDetailURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
            googlePlacesDetailURL.append("placeid=" + placeID);
            googlePlacesDetailURL.append("&key=" + GOOGLE_API_KEY);
            PlacesDetailReadTask placesDetailReadTask = new PlacesDetailReadTask(this);
            Object[] toPass = new Object[1];
            toPass[0] = googlePlacesDetailURL.toString();
            placesDetailReadTask.execute(toPass);
        Log.d("DETAIL QUERY URL", googlePlacesDetailURL.toString());
    }
}
