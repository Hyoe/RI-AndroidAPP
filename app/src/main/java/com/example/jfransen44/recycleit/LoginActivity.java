package com.example.jfransen44.recycleit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {


    static String[] favPlaceIDList = null;
    static String[] favPlaceNameList = null;
    static HashMap<String, String> favMap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.login_button);

        final AutoCompleteTextView etLoginName = (AutoCompleteTextView) findViewById(R.id.login_name);
        final EditText etPassword = (EditText) findViewById(R.id.login_password);

        final TextView loginStatus = (TextView) findViewById(R.id.login_status);
        final TextView forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        final String forgotPWLink = "http://www.recycleit.site/includes/forgotPassword.php";

        forgotPassword.setText(Html.fromHtml("<a href=\""+forgotPWLink+"\">Forgot Password?</a>"));
        forgotPassword.setMovementMethod(LinkMovementMethod.getInstance());

        assert loginButton != null;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etLoginName != null ? etLoginName.getText().toString() : null;
                String password = etPassword != null ? etPassword.getText().toString() : null;

                android.util.Log.d("LOGIN", username);

                //check that all fields are not null
                assert loginStatus != null;
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    loginStatus.setText("All fields must be completed.");
                } else {
                    //everything is OK, build url string
                    password = Util.encryptPassword(password);
                    //example URL http://recycleit-1293.appspot.com/test?function=doLogin&username=test&password=9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08
                    String myURL = "http://recycleit-1293.appspot.com/test?function=doLogin&username="
                            + username + "&password=" + password;


                    try {
                        String[] url = new String[]{myURL};
                        String output = new GetData().execute(url).get();
                        JSONObject jObject = new JSONObject(output);
                        String status = jObject.getString("status");


                        //String[] favorites = jObject.value.getJSONArray("favorites");

                        //status can be incorrectUsernamePassword, Login Successful
                        if (status.equals("incorrectUsernamePassword")) {
                            loginStatus.setText("Incorrect Login Credentials");
                        } else {
                            //good login, save variables and return to main activity

                            JSONArray jArray = jObject.getJSONArray("favorites"); //wont work if the login was invalid
                            int arrSize = jArray.length(); //goes with above line
                            favPlaceIDList = new String[arrSize];
                            favPlaceNameList = new String[arrSize];
                            //this populates a String array
                            //jObject = jArray.getJSONObject(0);
                            //if (jObject.getJSONObject("favorites") != null) {

                            //Toast.makeText(LoginActivity.this, "array length: " + arrSize, Toast.LENGTH_LONG).show();
                            favMap = new HashMap<String, String>();
                            for (int i = 0; i < arrSize; i++) {
                                jObject = jArray.getJSONObject(i);
                                favPlaceIDList[i] = jObject.getString("placeid");
                                favPlaceNameList[i] = jObject.getString("placename");
                                favMap.put(favPlaceIDList[i], favPlaceNameList[i]);
                                //Toast.makeText(LoginActivity.this, favPlaceNameList[i], Toast.LENGTH_SHORT).show();
                                Log.d("FAV IDS", favPlaceIDList[i]);
                            }



                            loginStatus.setText("Login Successful");
                            Intent intent = new Intent();
                            intent.putExtra("username", username);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                    } catch (Exception e) {
                        loginStatus.setText(e.toString()); //TEMP - for testing

                    }
                }

            }
        });
    }
}
