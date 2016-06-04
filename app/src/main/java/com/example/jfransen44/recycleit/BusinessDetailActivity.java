package com.example.jfransen44.recycleit;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class BusinessDetailActivity extends AppCompatActivity {

    ImageView businessImage;
    TextView businessName;
    TextView businessAddress;
    TextView businessPhone;
    TextView businessURL;
    //TextView businessHours;
    TextView monday;
    TextView tuesday;
    TextView wednesday;
    TextView thursday;
    TextView friday;
    TextView saturday;
    TextView sunday;
    ImageView icon;

    CheckBox favoritesCheckBox;
    CheckBox reimbursableCheckBox;
    boolean loggedIn;
    String userName;
    String placeID;
    String[] dbUpdateString = new String[5];
    String materialsAccepted = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_business_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        favoritesCheckBox = (CheckBox)findViewById(R.id.favoritesCheckBox);
        reimbursableCheckBox = (CheckBox)findViewById(R.id.reimbursableCheckBox);
        businessName = (TextView) findViewById(R.id.businessName);
        businessAddress = (TextView) findViewById(R.id.businessAddress);
        businessPhone = (TextView) findViewById(R.id.businessPhone);
        //businessHours = (TextView) findViewById(R.id.businessHours);
        businessURL = (TextView) findViewById(R.id.businessURL);
        monday = (TextView) findViewById(R.id.Monday);
        tuesday = (TextView) findViewById(R.id.Tuesday);
        wednesday = (TextView) findViewById(R.id.Wednesday);
        thursday = (TextView) findViewById(R.id.Thursday);
        friday = (TextView) findViewById(R.id.Friday);
        saturday = (TextView) findViewById(R.id.Saturday);
        sunday = (TextView) findViewById(R.id.Sunday);
        icon = (ImageView) findViewById(R.id.imageURL);
        final List <String> materialsAcceptedList = new ArrayList<String>();


        placeID = getIntent().getExtras().getString("placeID");
        userName = getIntent().getExtras().getString("userName");

        Button updateButton = (Button) findViewById(R.id.updateButton);

        if (userName != null){
            favoritesCheckBox.setVisibility(View.VISIBLE);
            reimbursableCheckBox.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(BusinessDetailActivity.this, "Log In to make changes", Toast.LENGTH_LONG).show();
        }
        assert updateButton != null;
        //TODO  Check to make sure isFavorite is not already in users list of favorites. Make DB call.  Pass isFavorite, materialsAccepted.
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (materialsAccepted.length() > 0) {
                    materialsAccepted = materialsAccepted.substring(0, materialsAccepted.length() - 2);
                }
                Log.d("ACCEPTEDSTRING", materialsAccepted);

                if (userName != null) {

                    //TODO undo hardcoded username
                    dbUpdateString[0] = userName;
                    dbUpdateString[1] = placeID;


                    if (favoritesCheckBox.isChecked()) {
                        dbUpdateString[2] = "1";
                    }
                    else{
                        dbUpdateString[2] = "0";
                    }
                    if (reimbursableCheckBox.isChecked()) {
                        dbUpdateString[3] = "1";
                    }
                    else{
                        dbUpdateString[3] = "0";
                    }
                    dbUpdateString[4] = materialsAccepted;

                    for (int i = 0; i < dbUpdateString.length; i++) {
                        Log.d("DBUPDATESTRING", dbUpdateString[i]);
                    }
                    String myURL = "http://recycleit-1293.appspot.com/test?function=updateFavorite&username="
                            + MainActivity.session_username + "&place_id=" + MainActivity.place_id + "&favorite_checked=" + dbUpdateString[2];

                    try {
                        String[] url = new String[]{myURL};
                        String output = new GetData().execute(url).get();
                        JSONObject jObject = new JSONObject(output);
                        String status = jObject.getString("status");

                        if (status.equals("validUpdate")) {
                            Toast.makeText(BusinessDetailActivity.this, "Saved.", Toast.LENGTH_SHORT).show();
                        } else {//database not written to
                            Toast.makeText(BusinessDetailActivity.this, "Error - not saved.", Toast.LENGTH_SHORT).show();
                        }
                    }
                        catch(Exception e) {
                        Log.d("Log.INFO", e.toString());

                }

            }
                else {
                    Toast.makeText(BusinessDetailActivity.this, "You must log in to make changes.", Toast.LENGTH_LONG).show();
                    Log.e("session_username", "is null");
                }
            }

        });


        final List<String> list = Arrays.asList("Aluminum", "Copper", "Electronics", "Glass", "Household Hazardous Waste", "Paper", "Plastic", "Steel");
        TreeMap<String, Boolean> items = new TreeMap<>();
        for(String item : list) {
            items.put(item, Boolean.FALSE);
        }
        MultiSpinner simpleSpinner = (MultiSpinner) findViewById(R.id.simpleMultiSpinner);
        simpleSpinner.setPrompt("Select Materials Accepted");

        simpleSpinner.setItems(items, new MultiSpinner.MultiSpinnerListener() {

            @Override
            public void onItemsSelected(boolean[] selected) {
                // your operation with code...
                materialsAccepted = "";
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        //materialsAcceptedList.add(list.get(i));
                        materialsAccepted += list.get(i) + ", ";
                        Log.i("TAG", i + " : " + materialsAccepted);
                    }
                }
            }
        });


        /* Contents of businessDetail array
        Index:
        0: Business Name
        1: Business Address
        2: Business Phone Number
        3: Business Photo URL
        4: Business Website URL
        5: Business Hours, separated by comma
         */
        loggedIn = getIntent().getBooleanExtra("loggedIn", false);

        if (loggedIn){
            favoritesCheckBox.setVisibility(View.VISIBLE);
            reimbursableCheckBox.setVisibility(View.VISIBLE);
         }
        String[] businessDetail = this.getIntent().getStringArrayExtra("businessDetails");

        Log.d("BUSINESS DETAIL URL", ">"+businessDetail[3]+"<");

        String[] workweek = businessDetail[5].split("\\s*\",\"\\s*");
        if (businessDetail[3].equals("https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png")){
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.recycliticon1);
            icon.setImageBitmap(bm);

        }else{
            //icon.setImageDrawable(LoadImageFromWebOperations(businessDetail[3]));
            //icon.setImageBitmap(getBitmapFromURL(businessDetail[3]));
            new ImageLoadTask(businessDetail[3].toString(), icon).execute();
        }
        businessName.setText(businessDetail[0]);
        businessAddress.setText(businessDetail[1]);
        businessPhone.setText(businessDetail[2]);
        businessURL.setText(Html.fromHtml("<a href=\""+businessDetail[4]+"\">Website</a>"));
        businessURL.setMovementMethod(LinkMovementMethod.getInstance());
        if (workweek[0].equals("HOURS UNAVAILABLE")){
            monday.setText("HOURS UNAVAILABLE");
        }
        else {
            monday.setText(workweek[0].replace('"', ' '));
            tuesday.setText(workweek[1].replace('"', ' '));
            wednesday.setText(workweek[2].replace('"', ' '));
            thursday.setText(workweek[3].replace('"', ' '));
            friday.setText(workweek[4].replace('"', ' '));
            saturday.setText(workweek[5].replace('"', ' '));
            sunday.setText(workweek[6].replace('"', ' '));
        }
    }
}


