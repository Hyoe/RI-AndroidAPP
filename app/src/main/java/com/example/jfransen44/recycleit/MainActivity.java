package com.example.jfransen44.recycleit;

import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final LatLng csumbLatLng = new LatLng(36.654458, -121.801567);
    private final float defaultZoom = (float) 16.0;
    private GoogleApiClient mGoogleApiClient;
    private Button zipSearchButton;
    private EditText zipTextBox;
    private String zipCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        zipSearchButton = (Button) findViewById(R.id.zipSearchButton);
        zipTextBox = (EditText) findViewById(R.id.zipTextBox);

        //set zipSearchButton listener
        zipSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipCode = zipTextBox.getText().toString();
                //check for 5 digit zip, make alert if not
                if(zipCode.length() != 5){
                    Toast.makeText(MainActivity.this, "Enter valid zip code", Toast.LENGTH_SHORT).show();
                }
                else{
                    LatLng newZip = getLocatonFromZip(this, zipCode);
                    mMap.addMarker(new MarkerOptions().position(newZip).title(zipCode));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newZip, defaultZoom));
                }

            }
        });

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                //.enableAutoManage(this, this)
                .build();
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
        //mMap.setMyLocationEnabled(true);  To Be Enabled if location services set up
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        // Add a marker at CSUMB and move the camera
        mMap.addMarker(new MarkerOptions().position(csumbLatLng).title("CSUMB"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(csumbLatLng, defaultZoom));
    }

    public LatLng getLocatonFromZip(View.OnClickListener context, String zipCode){
        Geocoder coder = new Geocoder(this);
        List<android.location.Address> address;
        LatLng userZip = null;

        try{
            address = coder.getFromLocationName(zipCode, 5);
            if (address == null) {
                return null;
            }
            android.location.Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            userZip = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return userZip;
    }

}
