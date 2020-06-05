package com.labstechnology.project1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class AnnouncementActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "AnnouncementActivity";

    private androidx.appcompat.widget.Toolbar toolbar;
    private FloatingActionButton btnAddAnnouncements;
    private RecyclerView recyclerViewAnnouncements;
    private BottomNavigationView bottomNavigationView;

    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        initViews();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Announcements");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white1));

        initBottomNavigation();

        initRecView();

        utils = new Utils(this);

        if (!utils.isAdmin()) {
            btnAddAnnouncements.setVisibility(View.GONE);
        } else {
            btnAddAnnouncements.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogAddAnnouncement dialogAddAnnouncement = new DialogAddAnnouncement();
                    dialogAddAnnouncement.show(getSupportFragmentManager(), "add announcement dialog");
                }
            });
        }

    }

    private void initRecView() {
        Log.d(TAG, "initRecView: called");
        //TODO: populate recycler views
    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        toolbar = (Toolbar) findViewById(R.id.announcements_toolbar);
        recyclerViewAnnouncements = (RecyclerView) findViewById(R.id.recViewAnnouncements);
        btnAddAnnouncements = (FloatingActionButton) findViewById(R.id.btnAddAnnouncements);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

        }
        return false;
    }

    private void initBottomNavigation() {
        Log.d(TAG, "initBottomNavigation: called");
        bottomNavigationView.setSelectedItemId(R.id.announcements);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(AnnouncementActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.live:
                        Toast.makeText(AnnouncementActivity.this, "Live selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tests:
                        Toast.makeText(AnnouncementActivity.this, "Tests selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.announcements:
                        Toast.makeText(AnnouncementActivity.this, "Already in Announcements", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
                return true;
            }
        });

    }
}
