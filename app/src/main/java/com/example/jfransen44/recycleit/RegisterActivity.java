package com.example.jfransen44.recycleit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
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
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mRegisterFormView;
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
                    registerStatus.setText("trying to register."); //TEMP - for testing
                    password1 = Util.encryptPassword(password1); //save encryption for later
                    String myURL = "http://recycleit-1293.appspot.com/test?function=doRegister&username="
                            + username + "&email=" + email + "&password=" + password1;

                    //on successful register, log user in and finish() activity
                    try {
                        String[] url = new String[]{myURL};
                        String output = new GetData().execute(url).get();
                        JSONObject jObject = new JSONObject(output);
                        String status = jObject.getString("status");
                        registerStatus.setText(status); //TEMP - for testing

                        //status can be: usernameTaken, successfulRegistration, databaseError
                        if (status.equals("usernameTaken")) {
                            registerStatus.setText("That Username is already taken.");
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

            /*




                    mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
                    populateAutoComplete();

                    mFirstName = (EditText) findViewById(R.id.editText);
                    mFirstName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                                attemptLogin();
                                return true;
                            }
                            return false;
                        }
                    });

                    mLastName = (EditText) findViewById(R.id.editText2);
                    mLastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                                attemptLogin();
                                return true;
                            }
                            return false;
                        }
                    });


                    mPasswordView = (EditText) findViewById(R.id.password);
                    mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                                attemptLogin();
                                return true;
                            }
                            return false;
                        }
                    });

                    Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
                    mEmailSignInButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            attemptLogin();
                        }
                    });

                    mRegisterFormView = findViewById(R.id.login_form);
                    mProgressView = findViewById(R.id.login_progress);
                }
            */
/*
            private void populateAutoComplete() {
                if (!mayRequestContacts()) {
                    return;
                }

                getLoaderManager().initLoader(0, null, this);
            }

            private boolean mayRequestContacts() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    return true;
                }
                if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
                if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
                    Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                            .setAction(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                @TargetApi(Build.VERSION_CODES.M)
                                public void onClick(View v) {
                                    requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                                }
                            });
                } else {
                    requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                }
                return false;
            }
*/
            /**
             * Callback received when a permissions request has been completed.
             */

            /*
            //@Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                   @NonNull int[] grantResults) {
                if (requestCode == REQUEST_READ_CONTACTS) {
                    if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        populateAutoComplete();
                    }
                }
            }
*/

            /**
             * Attempts to sign in or register the account specified by the login form.
             * If there are form errors (invalid email, missing fields, etc.), the
             * errors are presented and no actual login attempt is made.
             */
    /*
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mFirstName.setError(null);
        mLastName.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid first name
        if (TextUtils.isEmpty(firstName)){
            mFirstName.setError("First Name Required.");
            focusView = mFirstName;
            cancel = true;
        }

        // Check for a valid last name
        if (TextUtils.isEmpty(lastName)){
            mLastName.setError("Last Name Required.");
            focusView = mLastName;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }
*/
            private boolean isEmailValid(CharSequence target) {
                //TODO: Replace this with your own logic
                return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
                //return email.contains("@");
            }

            private boolean isPasswordValid(String password) {
                //TODO: Replace this with your own logic
                return password.length() > 4;
            }

            /**
             * Shows the progress UI and hides the login form.
             */
            @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
            private void showProgress(final boolean show) {
                // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
                // for very easy animations. If available, use these APIs to fade-in
                // the progress spinner.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });

                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    mProgressView.animate().setDuration(shortAnimTime).alpha(
                            show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });
                } else {
                    // The ViewPropertyAnimator APIs are not available, so simply show
                    // and hide the relevant UI components.
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            }

        /*
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                return new CursorLoader(this,
                        // Retrieve data rows for the device user's 'profile' contact.
                        Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                                ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                        // Select only email addresses.
                        ContactsContract.Contacts.Data.MIMETYPE +
                                " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                        .CONTENT_ITEM_TYPE},

                        // Show primary email addresses first. Note that there won't be
                        // a primary email address if the user hasn't specified one.
                        ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
            }
*/
            /*
            @Override
            public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
                List<String> emails = new ArrayList<>();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    emails.add(cursor.getString(ProfileQuery.ADDRESS));
                    cursor.moveToNext();
                }

                addEmailsToAutoComplete(emails);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {

            }

            private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
                //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(RegisterActivity.this,
                                android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

                mEmailView.setAdapter(adapter);
            }


            private interface ProfileQuery {
                String[] PROJECTION = {
                        ContactsContract.CommonDataKinds.Email.ADDRESS,
                        ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
                };

                int ADDRESS = 0;
                int IS_PRIMARY = 1;
            }
*/
            /**
             * Represents an asynchronous login/registration task used to authenticate
             * the user.
             */
           /*
            public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

                private final String mEmail;
                private final String mPassword;

                UserLoginTask(String email, String password) {
                    mEmail = email;
                    mPassword = password;
                }

                @Override
                protected Boolean doInBackground(Void... params) {
                    // TODO: attempt authentication against a network service.

                    try {
                        // Simulate network access.
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        return false;
                    }

                    for (String credential : DUMMY_CREDENTIALS) {
                        String[] pieces = credential.split(":");
                        if (pieces[0].equals(mEmail)) {
                            // Account exists, return true if the password matches.
                            return pieces[1].equals(mPassword);
                        }
                    }

                    // TODO: register the new account here.
                    return true;
                }

                @Override
                protected void onPostExecute(final Boolean success) {
                    mAuthTask = null;
                    showProgress(false);

                    if (success) {
                        Intent intent = new Intent();
                        // TODO replace dummy names with jObject.getString("username")/firstName/lastName
                        intent.putExtra("username", "yomama");
                        intent.putExtra("firstName", "Joe");
                        intent.putExtra("lastName", "Blow");

                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                }

                @Override
                protected void onCancelled() {
                    mAuthTask = null;
                    showProgress(false);
                }
            }
*/
        }

