package com.labstechnology.project1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.labstechnology.project1.CallBacks.FireBaseCallBack;
import com.labstechnology.project1.models.User;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private androidx.appcompat.widget.Toolbar toolbar;
    private TextView textName, textEnrollment, textEmail, textMobNo, textGender;
    private RoundedImageView imageProfile;

    private User user;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();

        utils = new Utils(this);
        utils.getCurrentUser(new FireBaseCallBack() {
            @Override
            public void onSuccess(Object object) {
                user = (User) object;
                if (user != null) {
                    textEmail.setText(user.getEmailId());
                    textEnrollment.setText(user.getEnrollmentNo());
                    textGender.setText(user.getGender());
                    if (user.getMobileNo() == null) {
                        textMobNo.setVisibility(View.GONE);
                    } else {
                        textMobNo.setText(user.getMobileNo());
                    }

                    String name = user.getFirstName().substring(0, 1).toUpperCase() +
                            user.getFirstName().substring(1, user.getFirstName().length()).toLowerCase() + " " +
                            user.getLastName().substring(0, 1).toUpperCase() +
                            user.getLastName().substring(1, user.getLastName().length()).toLowerCase();
                    textName.setText(name);

                    Glide.with(ProfileActivity.this)
                            .asBitmap()
                            .load(user.getProfileImage())
                            .into(imageProfile);

                }
            }

            @Override
            public void onError(Object object) {

            }
        });

        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setTitle("Profile");
        toolbar.setTitleTextColor(getColor(R.color.white1));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);

    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        textEmail = (TextView) findViewById(R.id.textEmail);
        textName = (TextView) findViewById(R.id.textName);
        textEnrollment = (TextView) findViewById(R.id.textEnrollment);
        textMobNo = (TextView) findViewById(R.id.textMobNo);
        textGender = (TextView) findViewById(R.id.textGender);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageProfile = (RoundedImageView) findViewById(R.id.imageProfile);
    }

    @Override
    public boolean onNavigateUp() {

        onBackPressed();
        return true;
    }
}