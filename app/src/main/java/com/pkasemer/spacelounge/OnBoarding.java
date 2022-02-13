package com.pkasemer.spacelounge;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.pkasemer.spacelounge.Adapters.OnboardingSliderAdapter;

public class OnBoarding extends AppCompatActivity {


        //Variables
        ViewPager viewPager;
        LinearLayout dotsLayout;
        OnboardingSliderAdapter onboardingSliderAdapter;
        TextView[] dots;
        Button letsGetStarted;
        Animation animation;
        int currentPos;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_on_boarding);

            ActionBar actionBar = getSupportActionBar(); // or getActionBar();
            getSupportActionBar().setTitle("My new title"); // set the top title
            String title = actionBar.getTitle().toString(); // get the title
            actionBar.hide();

            //Hooks
            viewPager = findViewById(R.id.slider);
            dotsLayout = findViewById(R.id.dots);
            letsGetStarted = findViewById(R.id.get_started_btn);

            //Call adapter
            onboardingSliderAdapter = new OnboardingSliderAdapter(this);
            viewPager.setAdapter(onboardingSliderAdapter);

            //Dots
            addDots(0);
            viewPager.addOnPageChangeListener(changeListener);

            findViewById(R.id.get_started_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(OnBoarding.this, LoginMaterial.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        public void skip(View view) {
            startActivity(new Intent(this, LoginMaterial.class));
            finish();
        }

        public void next(View view) {
            viewPager.setCurrentItem(currentPos + 1);
        }

        private void addDots(int position) {

            dots = new TextView[4];
            dotsLayout.removeAllViews();

            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(this);
                dots[i].setText(Html.fromHtml("•"));
                dots[i].setTextSize(35);

                dotsLayout.addView(dots[i]);
            }

            if (dots.length > 0) {
                dots[position].setTextColor(getResources().getColor(R.color.black));
            }

        }

        ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDots(position);
                currentPos = position;

                if (position == 0) {
                    letsGetStarted.setVisibility(View.INVISIBLE);
                } else if (position == 1) {
                    letsGetStarted.setVisibility(View.INVISIBLE);
                } else if (position == 2) {
                    letsGetStarted.setVisibility(View.INVISIBLE);
                } else {
                    animation = AnimationUtils.loadAnimation(OnBoarding.this, R.anim.bottom_anim);
                    letsGetStarted.setAnimation(animation);
                    letsGetStarted.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

