package com.example.jfransen44.recycleit;

import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jfransen44.recycleit.MultiSpinner.MultiSpinnerListener;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
public class BusinessDetailActivity extends AppCompatActivity {

    ImageView businessImage;
    TextView businessName;
    TextView businessAddress;
    TextView businessPhone;
    TextView businessURL;
    TextView businessHours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_business_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        businessName = (TextView) findViewById(R.id.businessName);
        businessAddress = (TextView) findViewById(R.id.businessAddress);
        businessPhone = (TextView) findViewById(R.id.businessPhone);
        businessHours = (TextView) findViewById(R.id.businessHours);
        businessURL = (TextView) findViewById(R.id.businessURL);

        businessImage = (ImageView) findViewById(R.id.businessImage);
        //businessImage.
        final List<String> list = Arrays.asList("Aluminum", "Steel", "Copper", "Plastic", "Glass", "Paper", "Electronics", "Household Hazardous Waste");
        TreeMap<String, Boolean> items = new TreeMap<>();
        for(String item : list) {
            items.put(item, Boolean.FALSE);
        }
        MultiSpinner simpleSpinner = (MultiSpinner) findViewById(R.id.simpleMultiSpinner);
        simpleSpinner.setPrompt("Select Materials Accepted");

        simpleSpinner.setItems(items, new MultiSpinnerListener() {

            @Override
            public void onItemsSelected(boolean[] selected) {

                // your operation with code...
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        Log.i("TAG", i + " : " + list.get(i));
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
        String[] businessDetail = this.getIntent().getStringArrayExtra("businessDetails");
        for (int i = 0; i < 6; i++){
            Log.d("DETAIL ACT", businessDetail[i]);
        }
        businessDetail[5].replace(',', '\n');
        businessName.setText(businessDetail[0]);
        businessAddress.setText(businessDetail[1]);
        businessPhone.setText(businessDetail[2]);
        businessHours.setText(businessDetail[5]);
        businessURL.setText(businessDetail[4]);
    }

    public Drawable loadImageFromWeb (String url){
        Drawable d = new Drawable() {
            @Override
            public void draw(Canvas canvas) {

            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
        try{
            InputStream is = (InputStream) new URL(url).getContent();
            d = Drawable.createFromStream(is,"src name");

        }
        catch (Exception e){
            Log.d("loadImageFromWeb", e.toString());
        }
        return d;
    }
}
