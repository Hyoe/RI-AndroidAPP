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

        userName = getIntent().getExtras().getString("userName");
        placeID = getIntent().getExtras().getString("placeID");

        Button updateButton = (Button) findViewById(R.id.updateButton);
        assert updateButton != null;
        //TODO  Check to make sure isFavorite is not already in users list of favorites. Make DB call.  Pass isFavorite, materialsAccepted.
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO remove logged = true after testing
                for (int i = 0; i < materialsAcceptedList.size(); i++) {
                    if (i != materialsAcceptedList.size() - 1) {
                        materialsAccepted += materialsAcceptedList.get(i) + ", ";
                    } else {
                        materialsAccepted += materialsAcceptedList.get(i);
                    }
                }

                Log.d("ACCEPTEDSTRING", materialsAccepted);
                loggedIn = true;
                if (loggedIn) {

                    //TODO undo hardcoded username
                    //dbUpdateString[0] = userName;
                    dbUpdateString[0] = "TEST";
                    dbUpdateString[1] = placeID;

                    if (favoritesCheckBox.isChecked()) {
                        dbUpdateString[2] = "true";
                    }
                    else{
                        dbUpdateString[2] = "false";
                    }
                    if (reimbursableCheckBox.isChecked()) {
                        dbUpdateString[3] = "true";
                    }
                    else{
                        dbUpdateString[3] = "false";
                    }
                    dbUpdateString[4] = materialsAccepted;

                    for (int i = 0; i < dbUpdateString.length; i++) {
                        Log.d("DBUPDATESTRING", dbUpdateString[i]);
                    }
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
