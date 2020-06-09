package com.labstechnology.project1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.labstechnology.project1.models.Assignment;

import xyz.hasnat.sweettoast.SweetToast;

public class AssignmentActivity extends AppCompatActivity {
    private static final String TAG = "AssignmentActivity";


    private TextView textTitle, textDueDate, textCountDown;
    private Button btnAddSubmission, btnRemoveSubmission;
    private TextView textDesc;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Assignment incomingAssignment;
    private boolean isSubmitted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        initViews();


        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(getColor(R.color.white1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);

        Intent intent = getIntent();
        try {
            incomingAssignment = intent.getParcelableExtra("assignment");
            isSubmitted = intent.getBooleanExtra("isSubmitted", false);
            setViewsValues();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (isSubmitted) {
            btnAddSubmission.setText("Edit Submission");
            btnRemoveSubmission.setVisibility(View.VISIBLE);

            btnAddSubmission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUploadAssignment dialogUploadAssignment = new DialogUploadAssignment(AssignmentActivity.this, incomingAssignment, isSubmitted);
                    dialogUploadAssignment.show(getSupportFragmentManager(), "Dialog upload submission");
                }
            });

            btnRemoveSubmission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SweetToast.warning(AssignmentActivity.this, "Not configured yet");
                }
            });
        } else {
            btnAddSubmission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUploadAssignment dialogUploadAssignment = new DialogUploadAssignment(AssignmentActivity.this, incomingAssignment, isSubmitted);
                    dialogUploadAssignment.show(getSupportFragmentManager(), "Dialog upload submission");
                }
            });
        }


    }

    private void setViewsValues() {
        Log.d(TAG, "setViewsValues: called");
        textTitle.setText(incomingAssignment.getTitle());
        textDesc.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        textDueDate.setText(incomingAssignment.getDeadLineDate() + " " + incomingAssignment.getDeadLineTime());
        //TODO: format date Here
    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        textCountDown = (TextView) findViewById(R.id.textCountDown);
        textDesc = (TextView) findViewById(R.id.textDesc);
        textDueDate = (TextView) findViewById(R.id.textDueDate);
        btnAddSubmission = (Button) findViewById(R.id.btnAddSubmission);
        textTitle = (TextView) findViewById(R.id.textTitle);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnRemoveSubmission = (Button) findViewById(R.id.btnRemoveSubmission);

    }
}