package com.labstechnology.project1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labstechnology.project1.adapters.NotificationRecViewAdapter;
import com.labstechnology.project1.models.Announcement;

import java.util.ArrayList;


public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";

    private BottomNavigationView bottomNavigationView;
    private Utils utils;

    private RecyclerView notificationRecView, upcomingEventRecView, liveRecView, quizSummaryRecView;
    private NotificationRecViewAdapter notificationRecViewAdapter;


    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(view);

        initBottomNavigation();

        initRecView();

        utils = new Utils(getActivity());


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
                        if (utils.isNewAnnouncement(announcement)) {
                            announcements.add(0, announcement);
                            notificationRecViewAdapter.notifyDataSetChanged();
                        }
                    } catch (DatabaseException e) {
                        e.printStackTrace();
                        Log.d(TAG, "onDataChange: error occurred  at " + dataSnapshot.getChildren().toString() + e.getLocalizedMessage());
                    }


                }
                Log.d(TAG, "onDataChange: announcement:" + announcements);
                notificationRecViewAdapter.setItems(announcements);
                autoScroll();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void initRecView() {
        Log.d(TAG, "initRecView: called");

        notificationRecViewAdapter = new NotificationRecViewAdapter(getActivity());

        notificationRecView.setAdapter(notificationRecViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
                    private static final float SPEED = 400f;// Change this                value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

        };
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        notificationRecView.setLayoutManager(layoutManager);

//        notificationRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

    }

    private void initBottomNavigation() {
        Log.d(TAG, "initBottomNavigation: called");
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        break;
                    case R.id.live:
                        Toast.makeText(getActivity(), "Live selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tests:
                        Intent intentTest = new Intent(getActivity(), TestActivity.class);
                        startActivity(intentTest);
                        break;
                    case R.id.announcements:
                        Intent intent = new Intent(getActivity(), AnnouncementActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;

                }
                return true;
            }
        });

    }

    private void initViews(View view) {
        Log.d(TAG, "initViews: called");
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomNavigationView);
        liveRecView = (RecyclerView) view.findViewById(R.id.recViewLive);
        upcomingEventRecView = (RecyclerView) view.findViewById(R.id.recViewUpcomingEvents);
        quizSummaryRecView = (RecyclerView) view.findViewById(R.id.recViewQuizSummary);
        notificationRecView = (RecyclerView) view.findViewById(R.id.notificationRecView);

    }

    public void autoScroll() {
        final int speedScroll = 5000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                Log.d(TAG, "run: called");
                if (count == notificationRecViewAdapter.getItemCount())
                    count = 0;
                if (count < notificationRecViewAdapter.getItemCount()) {
                    try {
                        notificationRecView.smoothScrollToPosition(++count);
                        handler.postDelayed(this, speedScroll);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        handler.postDelayed(runnable, speedScroll);
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
}
