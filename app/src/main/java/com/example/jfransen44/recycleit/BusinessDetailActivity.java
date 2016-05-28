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

import java.io.InputStream;
import java.net.URL;

public class BusinessDetailActivity extends AppCompatActivity {

    ImageView businessImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_business_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        businessImage = (ImageView) findViewById(R.id.businessImage);
        String[] businessDetail = this.getIntent().getStringArrayExtra("businessDetails");
        for (int i = 0; i < 5; i++){
            Log.d("DETAIL ACT", businessDetail[i]);
        }
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
