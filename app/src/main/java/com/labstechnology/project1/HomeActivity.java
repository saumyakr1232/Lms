package com.labstechnology.project1;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    private androidx.appcompat.widget.Toolbar toolbar;


    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;
    private String currentUserID;



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

        initViews();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle("English Classes");
        toolbar.setSubtitle("by S B Patel Sir");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white1));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white1));
        toolbar.setTitleMarginStart(100);
        toolbar.setLogo(R.drawable.ic_computer);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        updateNavigationHeaderView();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new MainFragment());
        transaction.commit();




    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        toolbar = findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigationDrawer);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile:
                Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show();
            case R.id.home:
                Toast.makeText(this, "Already in Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.liveLecture:
                Toast.makeText(this, "Live selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.test:
                Toast.makeText(this, "Tests selected", Toast.LENGTH_SHORT).show();
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
                                intent1.putExtra("url", "http://labstechnologies.in/");
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
        View headerView = navigationView.inflateHeaderView(R.layout.navigation_header);
        final ImageView userProfilePhoto = (ImageView) headerView.findViewById(R.id.imageProfile);
        final TextView textName = (TextView) headerView.findViewById(R.id.txtName);
        final TextView textEnrollment = (TextView) headerView.findViewById(R.id.txtEnrollment1);

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
