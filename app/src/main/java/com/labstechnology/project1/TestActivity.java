package com.labstechnology.project1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labstechnology.project1.models.Assignment;

import java.util.ArrayList;
import java.util.Objects;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";

    private RecyclerView recyclerViewAssignments, recyclerViewQuizzes;
    private FloatingActionMenu fbMenu;
    private FloatingActionButton fabAddQuiz, fabAddAssignment;
    private androidx.appcompat.widget.Toolbar toolbar;
    private View view;

    private AssignmentRecViewAdapter assignmentRecViewAdapter;


    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initViews();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Tests");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);

        fabAddQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, AddQuizActivity.class);
                startActivity(intent);
            }
        });

        fabAddAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, AddAssignmentActivity.class);
                startActivity(intent);
            }
        });

        populateRecViews(this);


    }

    private void populateRecViews(Context context) {
        Log.d(TAG, "populateRecViews: called");

        assignmentRecViewAdapter = new AssignmentRecViewAdapter(context);
        recyclerViewAssignments.setAdapter(assignmentRecViewAdapter);
        recyclerViewAssignments.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));


        database = FirebaseDatabaseReference.DATABASE;

        myRef = database.getReference("assignments");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: snapshot" + dataSnapshot.toString());
                ArrayList<Assignment> assignments = new ArrayList<>();

                for (DataSnapshot oneSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Assignment assignment = oneSnapshot.getValue(Assignment.class);
                        Log.d(TAG, "onDataChange: assignment" + assignment);
                        Log.d(TAG, "onDataChange: assignment key" + oneSnapshot.getKey());
                        assignments.add(0, assignment);
                        assignmentRecViewAdapter.notifyDataSetChanged();
                    } catch (DatabaseException e) {
                        Log.d(TAG, "onDataChange: error occurred  at " + dataSnapshot.getChildren().toString() + e.getLocalizedMessage());
                        e.printStackTrace();
                    }


                }
                Log.d(TAG, "onDataChange: announcement:" + assignments);
                assignmentRecViewAdapter.setAssignments(assignments);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(TestActivity.this, "some error occurred" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        recyclerViewAssignments = (RecyclerView) findViewById(R.id.recViewAssignments);
        recyclerViewQuizzes = (RecyclerView) findViewById(R.id.recViewQuiz);
        fbMenu = (FloatingActionMenu) findViewById(R.id.fbMenuTests);
        fabAddAssignment = (FloatingActionButton) findViewById(R.id.fabAddAssignment);
        fabAddQuiz = (FloatingActionButton) findViewById(R.id.fabAddQuiz);
        toolbar = (Toolbar) findViewById(R.id.tests_toolbar);
        view = (View) findViewById(R.id.overLay);

    }
}