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
import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONObject;

/**
 * A register screen that requires username, email, and password.
 */
//public class RegisterActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
public class RegisterActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the register form.

        Button registerButton = (Button) findViewById(R.id.register_button);

        final AutoCompleteTextView etEmail = (AutoCompleteTextView) findViewById(R.id.register_email);
        final EditText etUsername = (EditText) findViewById(R.id.register_username);
        final EditText etPassword1 = (EditText) findViewById(R.id.register_password);
        final EditText etPassword2 = (EditText) findViewById(R.id.register_password_verf);

        final TextView registerStatus = (TextView) findViewById(R.id.register_status);

        assert registerButton != null;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get all fields as strings
                String email = etEmail != null ? etEmail.getText().toString() : null;
                String username = etUsername != null ? etUsername.getText().toString() : null;
                String password1 = etPassword1 != null ? etPassword1.getText().toString() : null;
                String password2 = etPassword2 != null ? etPassword2.getText().toString() : null;

                //check that all fields are not null
                assert registerStatus != null;
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
                    registerStatus.setText("All fields must be completed.");
                } else if (!password1.equals(password2)) {
                    registerStatus.setText("Passwords do not match.");
                } else if (!isEmailValid(email)) {
                    registerStatus.setText("Enter a valid email address.");
                } else if (!isPasswordValid(password1)) {
                    registerStatus.setText("Password is not valid.");
                } else {
                    //everything is OK, build url string
                    password1 = Util.encryptPassword(password1); //save encryption for later
                    String myURL = "http://recycleit-1293.appspot.com/test?function=doRegister&username="
                            + username + "&email=" + email + "&password=" + password1;

                    //on successful register, log user in and finish() activity
                    try {
                        String[] url = new String[]{myURL};
                        String output = new GetData().execute(url).get();
                        JSONObject jObject = new JSONObject(output);
                        String status = jObject.getString("status");

                        //status can be: usernameTaken, emailTaken, successfulRegistration, databaseError
                        if (status.equals("usernameTaken")) {
                            registerStatus.setText("This Username is already in use.");
                        } else if (status.equals("emailTaken")) {
                            registerStatus.setText("This Email is already in use.");
                        } else if (status.equals("successfulRegistration")) {
                            registerStatus.setText("Registration Successful");
                            Intent intent = new Intent();
                            intent.putExtra("username", username);
                            intent.putExtra("email", email);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            //database error
                            registerStatus.setText("Database Error, Please try later.");
                        }
                    } catch (Exception e) {
                        //LogPrinter(Log.INFO, e.toString());
                        registerStatus.setText(e.toString()); //TEMP - for testing
                    }


                }
            }
        });
    }

            private boolean isEmailValid(CharSequence target) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
            }

            private boolean isPasswordValid(String password) {
                //TODO: Include additional restrictions
                return password.length() > 4;
            }

    }

