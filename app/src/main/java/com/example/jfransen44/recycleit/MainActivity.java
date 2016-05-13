package com.example.jfransen44.recycleit;

import android.Manifest;
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

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationSource.OnLocationChangedListener {

    private GoogleMap mMap;
    private final LatLng csumbLatLng = new LatLng(36.654458, -121.801567);
    private final String GOOGLE_API_KEY = "AIzaSyDSoM54-pL-owZTO68KrTJM_OZ2utgt2Mo";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*show error if google play services unavailable
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }*/
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
        /*mMap = mapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //enable location services
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //mMap.setMyLocationEnabled(true);
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null){
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 2000, 0, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                MarkerOptions myMarker = new MarkerOptions();
                myMarker.title("Current Location");
                myMarker.position(latLng);
                mMap.addMarker(myMarker);
                getMapInfo(latLng);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });*/

        //mMap.setOnMarkerClickListener(this);



        // listview for menu
        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // activity title
        mActivityTitle = getTitle().toString();
        // add menu drawer list
        addDrawerItems();
        setupDrawer();
        // genereic listener for  menu list
        //mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //       Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
        //   }
        //});
        // set drawerlist listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //Intent a = new Intent(MainActivity.this, Activity1.class);
                        //startActivity(a);
                        Toast.makeText(MainActivity.this, "Login Pressed", Toast.LENGTH_SHORT).show();
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
                        //Intent b = new Intent(MainActivity.this, Activity2.class);
                        //startActivity(b);
                        Toast.makeText(MainActivity.this, "About Pressed", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
            }

        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        zipSearchButton = (Button) findViewById(R.id.zipSearchButton);
        //zipTextBox = (EditText) findViewById(R.id.zipTextBox);

        //set zipSearchButton listener
        zipSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //zipCode = newPlace;
                Log.d("ZIPTEXTBOX", newPlace.toString());
                //check for 5 digit zip, make alert if not

                mMap.clear();
                //LatLng newZip = getLocatonFromZip(this, zipCode);
                //Log.d("NEW ZIP", newZip.toString());
                getMapInfo(newPlace);
                mMap.addMarker(new MarkerOptions().position(newPlace).title(zipCode));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPlace, defaultZoom));

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
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

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

        // Add a marker at CSUMB and move the camera
        mMap.addMarker(new MarkerOptions().position(csumbLatLng).title("CSUMB"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(csumbLatLng, defaultZoom));

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null){
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 2000, 0, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                MarkerOptions myMarker = new MarkerOptions();
                myMarker.title("Current Location");
                myMarker.position(latLng);
                mMap.addMarker(myMarker);
                getMapInfo(latLng);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    //convert user entered zip code to lat/lon
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
        googlePlacesURL.append("&radius" + 5000);
        googlePlacesURL.append("&keyword=recycling");
        googlePlacesURL.append("&key=" + GOOGLE_API_KEY);

        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask(this);
        Object[] toPass = new Object[2];
        toPass[0] = mMap;
        toPass[1] = googlePlacesURL.toString();
        googlePlacesReadTask.execute(toPass);
    }

    // helper method for menu
    private void addDrawerItems() {
        String[] osArray = { "Login", "Favorite", "Comments", "About" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
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
        String placeResult = "";
        List<HashMap<String, String>> gPlacesList = parseResults(gQueryResult);
        int i = 0;
        //gPlacesList.get(1).get("place_name");
        //Log.d("Marker Place Name" , marker.getTitle());
        //Log.d("Place HashMap Place Name", placeResult);
        
        while (! marker.getTitle().contains(placeResult)){
            placeResult = gPlacesList.get(i).get("place_name");
            i++;
        }

        return false;
    }

    //get results string from GoogleReadTask query
    public void asyncResult(String result){
        if (result != null){
            Log.d("RESULT", result.toString());
            gQueryResult = result;
        }
        else
            Log.d("METHOD ASYNCRESULT", "result empty");
    }

    //parse results from GoogleReadTask query
    private List<HashMap<String, String>> parseResults(String queryResult){
        JSONObject gPlacesJson;
        List<HashMap<String, String>> parsedResultsList = null;
        GooglePlaces gPlacesParser = new GooglePlaces();

        try{
            gPlacesJson = new JSONObject((String) queryResult);
            parsedResultsList = gPlacesParser.parse(gPlacesJson);
        }
        catch (Exception e){
            Log.d("PARSERESULTS EXC", e.toString());
        }
        return parsedResultsList;
    }
}