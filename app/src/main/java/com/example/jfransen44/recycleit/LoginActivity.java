package com.example.jfransen44.recycleit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.login_button);

        final AutoCompleteTextView etLoginName = (AutoCompleteTextView) findViewById(R.id.login_name);
        final EditText etPassword = (EditText) findViewById(R.id.login_password);

        final TextView loginStatus = (TextView) findViewById(R.id.login_status);

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
                    String myURL = "http://recycleit-1293.appspot.com/test?function=doLogin&username="
                            + username + "&password=" + password;


                    try {
                        String[] url = new String[]{myURL};
                        String output = new GetData().execute(url).get();
                        JSONObject jObject = new JSONObject(output);
                        String status = jObject.getString("status");

                        //status can be incorrectUsernamePassword, Login Successful
                        if (status.equals("incorrectUsernamePassword")) {
                            loginStatus.setText("Incorrect Login Credentials");
                        } else {
                            //good login, save variables and return to main activity
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