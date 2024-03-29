package com.labstechnology.project1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.labstechnology.project1.CallBacks.FireBaseCallBack;
import com.labstechnology.project1.adapters.NotificationRecViewAdapter;
import com.labstechnology.project1.models.Announcement;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NotificationRecViewAdapter.DeleteAnnouncementNotification {
    private static final String TAG = "HomeActivity";

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View hView;
    private ProgressDialog progressDialog;
    private androidx.appcompat.widget.Toolbar toolbar;


    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;
    private String currentUserID;

    private Utils utils;



    private boolean value = Utils.value;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();


        userDatabase = FirebaseDatabaseReference.DATABASE.getReference().child(FirebaseConstants.USERS).child(currentUserID);

        utils = new Utils(this);

        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wail, this will take a moment.");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        initViews();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle("English Classes");
        toolbar.setSubtitle("by S B Patel Sir");
        toolbar.setTitleTextColor(Color.parseColor("#FCFEFE"));
        toolbar.setSubtitleTextColor(Color.parseColor("#FCFEFE"));
        toolbar.setTitleMarginStart(100);
        toolbar.setLogo(R.drawable.ic_computer);
        toolbar.setBackgroundColor(Color.parseColor("#3F3D56"));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        updateNavigationHeaderView(new FireBaseCallBack() {
            @Override
            public void onSuccess(Object object) {
                final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new MainFragment());
                transaction.commit();
                progressDialog.dismiss();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, 2000);

            }

            @Override
            public void onError(Object object) {

            }
        });


    }


    private void initViews() {
        Log.d(TAG, "initViews: called");
        toolbar = findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigationDrawer);
        hView = navigationView.getHeaderView(0);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile:
                Intent intentProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
            case R.id.home:
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.liveLecture:
                Intent intentLive = new Intent(HomeActivity.this, LiveActivity.class);
                startActivity(intentLive);
                break;
            case R.id.test:
                Intent intentTest = new Intent(HomeActivity.this, TestActivity.class);
                startActivity(intentTest);
                break;
            case R.id.announcements:
                final Intent intent = new Intent(HomeActivity.this, AnnouncementActivity.class);
                startActivity(intent);
                break;
            case R.id.aboutUs:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("About us")
                        .setMessage("Build and published by labs technology\n" +
                                "If you want an app or\n" +
                                "If you want to check our other services\n" +
                                "Click \"Ok\" to visit our website")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent1 = new Intent(HomeActivity.this, AboutUsWebActivity.class);
                                intent1.putExtra("url", "https://labstechnologies.in/");
                                startActivity(intent1);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.show();

                break;
            case R.id.setting:
                Intent intent1 = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent1);
                break;
            case R.id.rateUs:
                rateMe();
                break;
            case R.id.logOut:
                DialogLogOut dialogLogOut = new DialogLogOut();
                dialogLogOut.show(getSupportFragmentManager(), "log out dialog");
                break;
        }
        return false;
    }

    @Override
    public void onDeletingResult(Announcement announcement) {
        Log.d(TAG, "onDeletingResult: called");
        if (announcement != null) {
            utils.setAckAnnouncement(announcement);
            Log.d(TAG, "onDeletingResult: HERE" + utils.getAckAnnouncements().toString());
        }
    }

    private void rateMe() {

        //TODO: replace "com.android.chrome" with getPackageName()
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + "com.android.chrome")));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + "com.android.chrome")));
        }

    }

    private void updateNavigationHeaderView(final FireBaseCallBack callBack) {
        Log.d(TAG, "updateNavigationHeaderView: called");
        final RoundedImageView userProfilePhoto = (RoundedImageView) hView.findViewById(R.id.imageProfileNavHeader);
        final TextView textName = (TextView) hView.findViewById(R.id.txtName);
        final TextView textEnrollment = (TextView) hView.findViewById(R.id.txtEnrollment1);

        userDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey();
                    String value = Objects.requireNonNull(data.getValue()).toString();
                    assert key != null;
                    if (key.equals("firstName")) {
                        textName.setText("Hello, " + value);
                    }
                    if (key.equals("enrollmentNo")) {
                        textEnrollment.setText(value);
                    }
                    if (key.equals("profileImage")) {
                        Log.d(TAG, "onDataChange: imageUrl" + value);
                        Glide.with(HomeActivity.this)
                                .asBitmap()
                                .load(value)
                                .into(userProfilePhoto);

                    }
                    callBack.onSuccess(0);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
