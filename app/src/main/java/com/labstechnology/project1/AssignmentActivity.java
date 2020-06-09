package com.labstechnology.project1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.labstechnology.project1.CallBacks.FireBaseCallBack;
import com.labstechnology.project1.models.Assignment;
import com.labstechnology.project1.models.AssignmentResponse;
import com.labstechnology.project1.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import xyz.hasnat.sweettoast.SweetToast;

import static android.view.View.GONE;

public class AssignmentActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {
    private static final String TAG = "AssignmentActivity";


    private TextView textTitle, textDueDate, textCountDown, textFileName, textFileSize;
    private CardView cardThumbnail;
    private ImageView thumbnail;
    private Button btnAddSubmission, btnRemoveSubmission;
    private TextView textDesc;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Assignment incomingAssignment;
    private boolean isSubmitted;

    private StorageReference assignmentDocumentsRef, assignmentDocumentsRef2;
    private DatabaseReference rttAssDatabaseRef;
    private String id;

    private Utils utils;

    private String fileName, fileSize;
    private Bitmap bitmapThumbnail;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        initViews();

        utils = new Utils(AssignmentActivity.this);


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
            final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("assignments");
            id = myRef.push().getKey();

            rttAssDatabaseRef = FirebaseDatabaseReference.DATABASE.getReference()
                    .child(FirebaseConstants.ASSIGNMENTS).child(incomingAssignment.getId());


            assignmentDocumentsRef2 = FirebaseStorage.getInstance().getReference().child("assignmentDocumentsUsers")
                    .child(Utils.getCurrentUid());
            setViewsValues();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        final DialogUploadAssignment dialogUploadAssignment = new DialogUploadAssignment(AssignmentActivity.this, incomingAssignment, isSubmitted);

        btnRemoveSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentActivity.this)
                        .setTitle("Remove assignment")
                        .setMessage("Are you sure ?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteSubmission();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });

        btnAddSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUploadAssignment.show(getSupportFragmentManager(), "Dialog upload submission");


            }
        });


        changeViewsBasedOnStatus();

        dialogUploadAssignment.setDialogResult(new DialogUploadAssignment.OnUploadDialogResult() {
            @Override
            public void finish(HashMap<String, Object> result) {
                fileName = (String) result.get("fileName");
                fileSize = (String) result.get("fileSize");
                bitmapThumbnail = (Bitmap) result.get("thumbnail");
                Log.d(TAG, "finish: fileName" + fileName);
                Log.d(TAG, "finish: fileSize" + fileSize);
                changeViewsBasedOnStatus();
            }
        });

        long mills = 0;

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") Date date1 = null;
        Date time = null;
        try {
            date1 = new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(incomingAssignment.getDeadLineDate() + " " + incomingAssignment.getDeadLineTime());
            if (date1 != null) {
                mills = date1.getTime();
            }
            Log.d(TAG, "onCreate: date" + date1.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millsNow = System.currentTimeMillis();
        new CountDownTimer(mills - millsNow, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                long daysRemaining = millisUntilFinished / 86400000;
                long hrRemaining = (millisUntilFinished % 86400000) / 3600000;
                textCountDown.setText(daysRemaining + " Days " + hrRemaining + " hr Remaining");

            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                textCountDown.setText("Time's up");
            }
        }.start();


    }

    private void deleteSubmission() {
        Log.d(TAG, "deleteSubmission: called");
        Log.d(TAG, "uploadDocumentAndLinkToAssignment: called");

        utils.getCurrentUser(new FireBaseCallBack() {
            @Override
            public void onSuccess(Object object) {
                final User user = (User) object;
                HashMap<String, Object> uploadMap = new HashMap<>();


                Date date = new Date();
                long time = date.getTime();
//                                    Timestamp timestamp = new Timestamp(time);
                //Gson gson = new Gson();
                ArrayList<AssignmentResponse> assignmentResponses = incomingAssignment.getResponses();
                ArrayList<AssignmentResponse> newAssignmentResponses = new ArrayList<>();

                for (AssignmentResponse response : assignmentResponses
                ) {
                    if (response.getAssignmentId().equals(incomingAssignment.getId())) {
                        Log.d(TAG, "onSuccess: HERE" + response.getId());
                        assignmentDocumentsRef2.child(response.getId() + ".pdf").delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    SweetToast.success(AssignmentActivity.this, "Old file deleted successfully");
                                } else {
                                    Log.d(TAG, "onComplete: error" + task.getException().getMessage());
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: error while deleting old file" + e.getMessage());
                            }
                        });
                    } else {
                        newAssignmentResponses.add(response);
                    }
                }

                ArrayList<User> attemptedBy = incomingAssignment.getAttemptedBy();

                ArrayList<User> newAttemptedBy = new ArrayList<>();

                for (User u : attemptedBy
                ) {
                    if (!u.getuId().equals(user.getuId())) {
                        newAttemptedBy.add(u);
                    }
                }

                uploadMap.put("responses", newAssignmentResponses);
                uploadMap.put("attemptedBy", newAttemptedBy);


                rttAssDatabaseRef.updateChildren(uploadMap).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    SweetToast.success(AssignmentActivity.this, "Your submissions is Deleted successfully");

                                }
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        SweetToast.error(AssignmentActivity.this, "Unable to update Assignment attempted List");
                    }
                });

            }

            @Override
            public void onError(Object object) {
                try {
                    DatabaseError error = (DatabaseError) object;
                    Log.d(TAG, "onError: error" + error.getMessage());
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    private void changeViewsBasedOnStatus() {
        Log.d(TAG, "changeViewsBasedOnStatus: called");
        if (utils.isUserAttemptThisAssignment(incomingAssignment)) {
            Log.d(TAG, "changeViewsBasedOnStatus: user attempted this assignment " + utils.isUserAttemptThisAssignment(incomingAssignment));
            cardThumbnail.setVisibility(View.VISIBLE);
            textFileSize.setVisibility(View.VISIBLE);
            textFileName.setVisibility(View.VISIBLE);
            btnRemoveSubmission.setVisibility(View.VISIBLE);
            btnAddSubmission.setText("Edit submission");
            if (fileName != null) {
                textFileName.setText(fileName);
            }
            if (fileSize != null) {
                textFileSize.setText(fileSize);
            }
            if (bitmapThumbnail != null) {
                thumbnail.setImageBitmap(bitmapThumbnail);
            }

        } else {
            btnRemoveSubmission.setVisibility(View.GONE);
            textFileName.setVisibility(GONE);
            textFileSize.setVisibility(GONE);

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
        cardThumbnail = (CardView) findViewById(R.id.cardThumbnail);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        textFileName = (TextView) findViewById(R.id.textFileName);
        textFileSize = (TextView) findViewById(R.id.textFileSize);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        changeViewsBasedOnStatus();
    }
}