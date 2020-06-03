package com.labstechnology.project1;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.internal.Util;
import com.google.gson.reflect.TypeToken;
import com.labstechnology.project1.CallBacks.FireBaseCallBack;
import com.labstechnology.project1.adapters.NotificationRecViewAdapter;
import com.labstechnology.project1.models.Announcement;
import com.labstechnology.project1.models.Assignment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";

    private BottomNavigationView bottomNavigationView;
    private Utils utils;

    private RecyclerView notificationRecView, upcomingEventRecView, liveRecView, quizSummaryRecView;
    private NotificationRecViewAdapter notificationRecViewAdapter;


    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseRepository repository;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(view);

        initBottomNavigation();

        initRecView();


        database = FirebaseDatabaseReference.DATABASE;

        myRef = database.getReference("announcements2");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange: snapshot"+ dataSnapshot.toString());
//                ArrayList<Announcement> announcements = new ArrayList<>();
//
//                for (DataSnapshot oneSnapshot: dataSnapshot.getChildren()){
//                    Announcement announcement = oneSnapshot.getValue(Announcement.class);
//                    announcements.add(announcement);
//
//                }
//                Log.d(TAG, "onDataChange: announcement:" + announcements);
//                notificationRecViewAdapter.setItems(announcements);
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        readAnnouncements(new FireBaseCallBack() {
//            @Override
//            public void onSuccess(Object object) {
//                Announcement announcement = (Announcement) object;
//                Log.d(TAG, "onSuccess: announcement :"+ announcement.toString());
//            }
//
//            @Override
//            public void onError(Object object) {
//                FirebaseError error = (FirebaseError) object;
//                Log.d(TAG, "onError: "+error.getErrorCode());
//
//            }
//        });

        return view;
    }

    private void initRecView() {
        Log.d(TAG, "initRecView: called");

        notificationRecViewAdapter = new NotificationRecViewAdapter(getActivity());

        notificationRecView.setAdapter(notificationRecViewAdapter);

        notificationRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

    }

    private void initBottomNavigation() {
        Log.d(TAG, "initBottomNavigation: called");
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(getActivity(), "Already in Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.live:
                        Toast.makeText(getActivity(), "Live selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tests:
                        Toast.makeText(getActivity(), "Tests selected", Toast.LENGTH_SHORT).show();
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

    private void readAnnouncements(final FireBaseCallBack callBack) {
        Log.d(TAG, "readAnnouncements: called");
        DatabaseReference myRef = database.getReference("announcements/test");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Announcement announcement = dataSnapshot.getValue(Announcement.class);
                callBack.onSuccess(announcement);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callBack.onError(databaseError);
            }
        });
    }

}
