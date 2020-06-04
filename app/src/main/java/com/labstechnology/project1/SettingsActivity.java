package com.labstechnology.project1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    private DayNightSwitch btnDayNight;
    private CardView cardDarkMode, cardTermsAndCondition, cardTPartyNotice, cardPrivacyPolicy;
    private Utils utils;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        utils = new Utils(this);

        btnDayNight = (DayNightSwitch) findViewById(R.id.dayNightSwitch);
        cardPrivacyPolicy = (CardView) findViewById(R.id.CardPrivacyPolicy);
        cardTermsAndCondition = (CardView) findViewById(R.id.CardTermsAndCondition);
        cardTPartyNotice = (CardView) findViewById(R.id.CardTPartyNotice);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(getColor(R.color.white1));

        btnDayNight.setDuration(450);

        if (Utils.getDarkThemePreference(this)) {
            btnDayNight.setIsNight(true);
        } else {
            btnDayNight.setIsNight(false);
        }

        btnDayNight.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean is_night) {
                if (is_night) {
                    utils.setDarkThemePreference(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);


                } else {
                    utils.setDarkThemePreference(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                }
            }
        });


//        btnDayNight.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                if (btnDayNight.isChecked()) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this)
//                            .setMessage("Dark Mode is under development :")
//                            .setTitle("")
//                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    btnDayNight.setChecked(false);
//                                }
//                            });
//
//                    builder.show();
//
////                   utils.setDarkThemePreference(true);
////                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
////                   btnDayNight.setChecked(true);
//                } else {
////                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
////                   utils.setDarkThemePreference(false);
//                }
//            }
//        });

        cardPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Privacy policy")
                        .setMessage(Utils.getPrivacyPolicy())
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();

            }
        });

        cardTPartyNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Third party notices")
                        .setMessage(Utils.getTPartyNotices())
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });

        cardTermsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Terms and Conditions")
                        .setMessage(Utils.getTerms())
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });


    }
}
