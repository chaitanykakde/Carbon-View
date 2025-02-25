package com.chaitany.carbonemissions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class Onboard extends AppCompatActivity {

        private ViewPager2 onboardingViewPager;
        private Button skipButton;
        private Button getStartedButton;
        private LinearLayout indicatorsContainer;
        private OnboardingAdapter onboardingAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_onboard);

            onboardingViewPager = findViewById(R.id.onboardingViewPager);
            skipButton = findViewById(R.id.skipButton);
            getStartedButton = findViewById(R.id.getStartedButton);
            indicatorsContainer = findViewById(R.id.indicatorsContainer);

            setupOnboardingItems();
            setupIndicators();
            setCurrentIndicator(0);

            onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    setCurrentIndicator(position);
                    if (position == onboardingAdapter.getItemCount() - 1) {
                        skipButton.setVisibility(View.GONE);
                        getStartedButton.setVisibility(View.VISIBLE);
                    } else {
                        skipButton.setVisibility(View.VISIBLE);
                        getStartedButton.setVisibility(View.GONE);
                    }
                }
            });

            skipButton.setOnClickListener(v -> finishOnboarding());
            getStartedButton.setOnClickListener(v -> finishOnboarding());
        }

        private void setupOnboardingItems() {
            List<OnboardingItem> onboardingItems = new ArrayList<>();

            OnboardingItem page1 = new OnboardingItem();
            page1.setTitle("Track Your Carbon Footprint");
            page1.setDescription("Monitor your daily activities and their impact on the environment");
            page1.setImage(R.drawable.email);

            OnboardingItem page2 = new OnboardingItem();
            page2.setTitle("Analyze Your Impact");
            page2.setDescription("Get detailed insights about your carbon emissions");
            page2.setImage(R.drawable.email);

            OnboardingItem page3 = new OnboardingItem();
            page3.setTitle("Reduce Emissions");
            page3.setDescription("Learn actionable ways to reduce your carbon footprint");
            page3.setImage(R.drawable.email);

            OnboardingItem page4 = new OnboardingItem();
            page4.setTitle("Set Goals");
            page4.setDescription("Create personalized targets for emission reduction");
            page4.setImage(R.drawable.email);

            OnboardingItem page5 = new OnboardingItem();
            page5.setTitle("Track Progress");
            page5.setDescription("Monitor your improvement over time with detailed statistics");
            page5.setImage(R.drawable.email);

            OnboardingItem page6 = new OnboardingItem();
            page6.setTitle("Join the Community");
            page6.setDescription("Connect with others committed to environmental sustainability");
            page6.setImage(R.drawable.email);

            onboardingItems.add(page1);
            onboardingItems.add(page2);
            onboardingItems.add(page3);
            onboardingItems.add(page4);
            onboardingItems.add(page5);
            onboardingItems.add(page6);

            onboardingAdapter = new OnboardingAdapter(onboardingItems);
            onboardingViewPager.setAdapter(onboardingAdapter);
        }

        private void setupIndicators() {
            ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(8, 0, 8, 0);

            for (int i = 0; i < indicators.length; i++) {
                indicators[i] = new ImageView(getApplicationContext());
                indicators[i].setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.email
                ));
                indicators[i].setLayoutParams(layoutParams);
                indicatorsContainer.addView(indicators[i]);
            }
        }

        private void setCurrentIndicator(int position) {
            int childCount = indicatorsContainer.getChildCount();
            for (int i = 0; i < childCount; i++) {
                ImageView imageView = (ImageView) indicatorsContainer.getChildAt(i);
                if (i == position) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(
                            getApplicationContext(),
                            R.drawable.indicator_active
                    ));
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(
                            getApplicationContext(),
                            R.drawable.indicator_inactive
                    ));
                }
            }
        }

        private void finishOnboarding() {
            // Save that onboarding is completed
            SharedPreferences.Editor sharedPreferencesEditor =
                    getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
            sharedPreferencesEditor.putBoolean("isOnboardingCompleted", true);
            sharedPreferencesEditor.apply();

            // Navigate to main activity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }