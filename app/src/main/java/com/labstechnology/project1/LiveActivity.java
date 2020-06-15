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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labstechnology.project1.CallBacks.FireBaseCallBack;
import com.labstechnology.project1.adapters.LiveRecViewAdapter;
import com.labstechnology.project1.models.LiveEvent;

import java.util.ArrayList;
import java.util.Objects;

public class LiveActivity extends AppCompatActivity {
    private static final String TAG = "LiveActivity";

    private androidx.appcompat.widget.Toolbar toolbar;
    private FloatingActionButton btnAddLive;
    private RecyclerView recyclerViewLive;
    private BottomNavigationView bottomNavigationView;
    private LiveRecViewAdapter adapter;
    private ProgressBar progressBar;

    private Utils utils;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        initViews();
        initBottomNavigation();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle("LiveLectures");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);

        initRecView();


        utils = new Utils(this);
        progressBar.setVisibility(View.VISIBLE);

        database = FirebaseDatabaseReference.DATABASE;

        myRef = database.getReference("liveEvents");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: snapshot" + dataSnapshot.toString());
                ArrayList<LiveEvent> liveEvents = new ArrayList<>();

                for (DataSnapshot oneSnapshot : dataSnapshot.getChildren()) {
                    try {
                        LiveEvent liveEvent = oneSnapshot.getValue(LiveEvent.class);
                        Log.d(TAG, "onDataChange: announcement" + liveEvent);
                        Log.d(TAG, "onDataChange: announcement key" + oneSnapshot.getKey());
                        liveEvents.add(0, liveEvent);
                        adapter.notifyDataSetChanged();
                    } catch (DatabaseException e) {
                        Log.d(TAG, "onDataChange: error occurred  at " + dataSnapshot.getChildren().toString() + e.getLocalizedMessage());
                        e.printStackTrace();
                    }


                }
                Log.d(TAG, "onDataChange: announcement:" + liveEvents);
                adapter.setLiveEvents(liveEvents);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LiveActivity.this, "some error occurred" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        utils.checkForAdmin(new FireBaseCallBack() {
            @Override
            public void onSuccess(Object object) {
                Boolean isAdmin = (Boolean) object;
                if (isAdmin) {
                    btnAddLive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogAddLiveEvent dialogAddLiveEvent = new DialogAddLiveEvent();
                            dialogAddLiveEvent.show(getSupportFragmentManager(), "add Live Event dialog");
                        }
                    });
                } else {
                    btnAddLive.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Object object) {

            }
        });

    }

    private void initRecView() {
        Log.d(TAG, "initRecView: called");
        adapter = new LiveRecViewAdapter(this);
        recyclerViewLive.setAdapter(adapter);
        recyclerViewLive.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        toolbar = (Toolbar) findViewById(R.id.announcements_toolbar);
        recyclerViewLive = (RecyclerView) findViewById(R.id.recViewLive);
        btnAddLive = (com.google.android.material.floatingactionbutton.FloatingActionButton) findViewById(R.id.btnAddAnnouncements);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void initBottomNavigation() {
        Log.d(TAG, "initBottomNavigation: called");
        bottomNavigationView.setSelectedItemId(R.id.live);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(LiveActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.live:

                        break;
                    case R.id.tests:
                        Intent intentTest = new Intent(LiveActivity.this, TestActivity.class);
                        startActivity(intentTest);
                        break;
                    case R.id.announcements:
                        Intent intentAnnouncement = new Intent(LiveActivity.this, AnnouncementActivity.class);
                        startActivity(intentAnnouncement);
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