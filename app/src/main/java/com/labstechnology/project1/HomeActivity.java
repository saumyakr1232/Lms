package com.labstechnology.project1;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.labstechnology.project1.adapters.NotificationRecViewAdapter;
import com.labstechnology.project1.models.Announcement;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NotificationRecViewAdapter.DeleteAnnouncementNotification {
    private static final String TAG = "HomeActivity";

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View hView;
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



        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new MainFragment());
        transaction.commit();




    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateNavigationHeaderView();
            }
        }, 2000);


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
                Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show();
            case R.id.home:
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.liveLecture:
                Toast.makeText(this, "Live selected", Toast.LENGTH_SHORT).show();
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

    private void updateNavigationHeaderView() {
        Log.d(TAG, "updateNavigationHeaderView: called");
        final ImageView userProfilePhoto = (ImageView) hView.findViewById(R.id.imageProfileNavHeader);
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
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
