package com.labstechnology.project1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labstechnology.project1.adapters.NotificationRecViewAdapter;
import com.labstechnology.project1.models.Announcement;

import java.util.ArrayList;

public class AnnouncementAsyncTask extends AsyncTask<Void, Integer, ArrayList<Announcement>> {
    private static final String TAG = "AnnouncementAsyncTask";
    private Context context;
    private NotificationRecViewAdapter adapter;
    private Utils utils;
    private int flag;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public AnnouncementAsyncTask(NotificationRecViewAdapter adapter, Context context, int flag) {
        this.context = context;
        this.adapter = adapter;
        this.flag = flag;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        utils = new Utils(context);

        database = FirebaseDatabaseReference.DATABASE;

        Announcement announcement = new Announcement("title", "someDescription",
                "some link", 2, 6, 2020, 12, 4);
        Announcement announcement1 = new Announcement("test", "test description", "some link"
                , 3, 6, 2020, 2, 3);

        myRef = database.getReference("announcements/" + announcement.getTitle());
        myRef.setValue(announcement);
        DatabaseReference mRef2 = database.getReference("announcements/" + announcement1.getTitle());
        mRef2.setValue(announcement1);

    }

    @Override
    protected ArrayList<Announcement> doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: called");

        myRef = database.getReference("announcements/test");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: snapshot" + dataSnapshot.toString());
                Announcement announcement2 = dataSnapshot.getValue(Announcement.class);
                Log.d(TAG, "onDataChange: snapshot " + announcement2.toString());
                Log.d(TAG, "onDataChange: snapshot 2 " + announcement2.getTitle());
                Log.d(TAG, "onDataChange: 3 " + announcement2.getResourceLink());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Announcement> announcements) {
        super.onPostExecute(announcements);
        Log.d(TAG, "onPostExecute: test " + "here here here");
    }
}
