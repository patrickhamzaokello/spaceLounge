package com.pkasemer.spacelounge;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pkasemer.spacelounge.HelperClasses.SharedPrefManager;
import com.pkasemer.spacelounge.HttpRequests.RequestHandler;
import com.pkasemer.spacelounge.HttpRequests.URLs;
import com.pkasemer.spacelounge.Models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginMaterial extends AppCompatActivity {

    TextView logoText, sloganText;
    ImageView image;
    Button callSignUp, loginbtn;
    TextInputLayout username_layout, password_layout;
    TextInputEditText inputTextUsername, inputTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login_material);

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("Zodongo Foods"); // set the top title
        String title = actionBar.getTitle().toString(); // get the title
        actionBar.hide();

        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, RootActivity.class));
            return;
        }

        //Hooks
        callSignUp = findViewById(R.id.signup_screen);
        loginbtn = findViewById(R.id.login_btn);
        logoText = findViewById(R.id.login_welcomeback);
        sloganText = findViewById(R.id.login_subtext);
        image = findViewById(R.id.loginlogo);

        username_layout = findViewById(R.id.login_username);
        password_layout = findViewById(R.id.login_password);

        inputTextUsername = findViewById(R.id.inputTextUsername);
        inputTextPassword = findViewById(R.id.inputTextPassword);


        //if user presses on login
        //calling the method login
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        //if user presses on not registered
        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                Intent intent = new Intent(LoginMaterial.this, RegisterMaterial.class);
//                Pair[] pairs = new Pair[6];
//                pairs[0] = new Pair<View, String>(image,"logo_image");
//                pairs[1] = new Pair<View, String>(logoText,"logo_text");
//                pairs[2] = new Pair<View, String>(sloganText,"logo_desc");
//                pairs[3] = new Pair<View, String>(username_layout,"username_input");
//                pairs[4] = new Pair<View, String>(password_layout,"password_input");
//                pairs[5] = new Pair<View, String>(loginbtn,"account_button");
//                pairs[6] = new Pair<View, String>(callSignUp,"account_change");
//
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginMaterial.this, pairs);
//                startActivity(intent, options.toBundle());
                startActivity(intent);
            }
        });


    }


    private void userLogin() {
        //first getting the values
        final String username = inputTextUsername.getText().toString();
        final String password = inputTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            inputTextUsername.setError("Please enter your username");
            inputTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            inputTextPassword.setError("Please enter your password");
            inputTextPassword.requestFocus();
            return;
        }

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                if(s.isEmpty()){
                    //show network error
                    showErrorAlert();
                    return;
                }

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        UserModel userModel = new UserModel(
                                userJson.getInt("id"),
                                userJson.getString("fullname"),
                                userJson.getString("username"),
                                userJson.getString("email"),
                                userJson.getString("phone"),
                                userJson.getString("address"),
                                userJson.getString("profileimage")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(userModel);

                        //starting the RootActivity activity
                        finish();
//                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        Intent intent = new Intent(LoginMaterial.this, RootActivity.class);
                        startActivity(intent);
                    } else {
                        //Invalid Username or Password
                        showInvalidUser();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

    private void showErrorAlert() {
        new SweetAlertDialog(
                this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Something went wrong!")
                .show();
    }

    private void showInvalidUser() {

        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Wrong Info")
                .setContentText("Invalid User or Password")
                .show();
    }
}