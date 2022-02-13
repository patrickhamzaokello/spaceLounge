package com.pkasemer.spacelounge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 5000;
    SharedPreferences onboarding_sharedPreferences;

    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_activity);

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("Zodongo Foods"); // set the top title
        String title = actionBar.getTitle().toString(); // get the title
        actionBar.hide();

        //animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.splashbottom_animation);

        image = findViewById(R.id.splashimage);

        image.setAnimation(topAnim);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                onboarding_sharedPreferences = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstTime = onboarding_sharedPreferences.getBoolean("firstTime", true);

                if(isFirstTime){

                    SharedPreferences.Editor editor = onboarding_sharedPreferences.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    Intent intent = new Intent(SplashActivity.this, OnBoarding.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginMaterial.class);
                    startActivity(intent);

//                    Pair[] pairs = new Pair[3];
//                    pairs[0] = new Pair<View, String>(image,"logo_image");
//                    pairs[1] = new Pair<View, String>(logo,"logo_text");
//                    pairs[2] = new Pair<View, String>(slogan,"logo_desc");
//
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs);
//                    startActivity(intent, options.toBundle());
                }
                finish();


            }
        }, SPLASH_SCREEN);

    }

}