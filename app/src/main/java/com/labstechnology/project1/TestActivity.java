package com.labstechnology.project1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.Objects;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";

    private RecyclerView recyclerViewAssignments, recyclerViewQuizzes;
    private FloatingActionMenu fbMenu;
    private FloatingActionButton fabAddQuiz, fabAddAssignment;
    private androidx.appcompat.widget.Toolbar toolbar;
    private View view;

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