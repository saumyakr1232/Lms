package com.labstechnology.project1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labstechnology.project1.adapters.AnnouncementRecViewAdapter;
import com.labstechnology.project1.models.Announcement;

import java.util.ArrayList;
import java.util.Objects;

public class AnnouncementActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "AnnouncementActivity";

    private androidx.appcompat.widget.Toolbar toolbar;
    private FloatingActionButton btnAddAnnouncements;
    private RecyclerView recyclerViewAnnouncements;
    private BottomNavigationView bottomNavigationView;
    private AnnouncementRecViewAdapter adapter;
    private ProgressBar progressBar;

    private Utils utils;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);


        initBottomNavigation();

        initRecView();

        utils = new Utils(this);
        progressBar.setVisibility(View.VISIBLE);

        database = FirebaseDatabaseReference.DATABASE;

        myRef = database.getReference("announcements");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: snapshot" + dataSnapshot.toString());
                ArrayList<Announcement> announcements = new ArrayList<>();

                for (DataSnapshot oneSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Announcement announcement = oneSnapshot.getValue(Announcement.class);
                        Log.d(TAG, "onDataChange: announcement" + announcement);
                        Log.d(TAG, "onDataChange: announcement key" + oneSnapshot.getKey());
                        announcements.add(0, announcement);
                        adapter.notifyDataSetChanged();
                    } catch (DatabaseException e) {
                        Log.d(TAG, "onDataChange: error occurred  at " + dataSnapshot.getChildren().toString() + e.getLocalizedMessage());
                        e.printStackTrace();
                    }


                }
                Log.d(TAG, "onDataChange: announcement:" + announcements);
                adapter.setAnnouncements(announcements);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AnnouncementActivity.this, "some error occurred" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


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
        adapter = new AnnouncementRecViewAdapter(this);
        recyclerViewAnnouncements.setAdapter(adapter);
        recyclerViewAnnouncements.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        toolbar = (Toolbar) findViewById(R.id.announcements_toolbar);
        recyclerViewAnnouncements = (RecyclerView) findViewById(R.id.recViewAnnouncements);
        btnAddAnnouncements = (FloatingActionButton) findViewById(R.id.btnAddAnnouncements);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


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
                        Intent intentTest = new Intent(AnnouncementActivity.this, TestActivity.class);
                        startActivity(intentTest);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
