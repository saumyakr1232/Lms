package com.labstechnology.project1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.hasnat.sweettoast.SweetToast;

import static android.view.View.GONE;

public class AssignmentActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {
    private static final String TAG = "AssignmentActivity";


    private TextView textTitle, textDueDate, textCountDown;
    private TextView textGradingStatus, textSubmitted, textFileDownloadSize;
    private CardView cardThumbnail;
    private ImageView thumbnail, thumbnailDownload, icDownloadAssignment, iconDownloadSubmission;
    private Button btnAddSubmission, btnRemoveSubmission;
    private TextView textDesc;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Assignment incomingAssignment;
    private Assignment rttAssignment;
    private boolean isSubmitted;

    private StorageReference assignmentDocumentsRef, assignmentDocumentsRef2;
    private DatabaseReference rttAssDatabaseRef;
    private String id;

    private Utils utils;

    private String fileName, fileSize;
    private Bitmap bitmapThumbnail;
    private long dateSubmission;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        initViews();

        utils = new Utils(AssignmentActivity.this);





        final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("assignments");

        Intent intent = getIntent();
        try {
            incomingAssignment = intent.getParcelableExtra("assignment");
            isSubmitted = intent.getBooleanExtra("isSubmitted", false);

            id = myRef.push().getKey();

            rttAssDatabaseRef = FirebaseDatabaseReference.DATABASE.getReference()
                    .child(FirebaseConstants.ASSIGNMENTS).child(incomingAssignment.getId());


            assignmentDocumentsRef2 = FirebaseStorage.getInstance().getReference().child("assignmentDocumentsUsers")
                    .child(Utils.getCurrentUid());
            setViewsValues();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle(incomingAssignment.getTitle());
        toolbar.setTitleTextColor(getColor(R.color.white1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);


        iconDownloadSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadSubmissionPdf();
            }
        });

        icDownloadAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAssignmentDocument();
            }
        });


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
        rttAssignment(myRef);


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

    private void downloadSubmissionPdf() {
        Log.d(TAG, "downloadSubmissionPdf: called");
    }

    private void downloadAssignmentDocument() {
        Log.d(TAG, "downloadAssignmentDocument: called");
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

                if (assignmentResponses != null) {
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
        if (rttAssignment != null) {
            if (utils.isUserAttemptThisAssignment(rttAssignment)) {
                cardThumbnail.setVisibility(View.VISIBLE);
                textFileDownloadSize.setVisibility(View.VISIBLE);
                btnRemoveSubmission.setVisibility(View.VISIBLE);
                btnAddSubmission.setText("Edit submission");

                if (fileSize != null) { //TODO: check
                    textFileDownloadSize.setText(fileSize);
                }
                if (bitmapThumbnail != null) {
                    thumbnail.setImageBitmap(bitmapThumbnail);
                }
                textGradingStatus.setText("Not Graded");
                    //TODO: set Date of submission
            } else {
                btnRemoveSubmission.setVisibility(View.GONE);
                textSubmitted.setVisibility(GONE);
                cardThumbnail.setVisibility(GONE);
                textGradingStatus.setVisibility(GONE);
            }
        } else {
            if (utils.isUserAttemptThisAssignment(incomingAssignment)) {
                cardThumbnail.setVisibility(View.VISIBLE);
                textSubmitted.setVisibility(View.VISIBLE);
                btnRemoveSubmission.setVisibility(View.VISIBLE);
                btnAddSubmission.setText("Edit submission");
                textSubmitted.setText("Submitted for grading");
                textGradingStatus.setText("Not graded");

                if (bitmapThumbnail != null) {
                    thumbnail.setImageBitmap(bitmapThumbnail);
                }

            } else {
                btnRemoveSubmission.setVisibility(View.GONE);
                textSubmitted.setVisibility(GONE);
                textGradingStatus.setVisibility(View.VISIBLE);

                cardThumbnail.setVisibility(GONE);

            }
        }
    }


    private void setViewsValues() {
        Log.d(TAG, "setViewsValues: called");
        textTitle.setText(incomingAssignment.getTitle());
        String description = incomingAssignment.getDescription();

        Pattern trimmer = Pattern.compile("(?:\\b(?:http|ftp|www\\.)\\S+\\b)|(?:\\b\\S+\\.com\\S*\\b)");
        Matcher m = trimmer.matcher(description);
        StringBuffer out = new StringBuffer();
        int i = 1;
        Log.d(TAG, "setViewsValues: Trimmer" + trimmer.toString());
        ArrayList<String> links = new ArrayList<>();
        while (m.find()) {
            Log.d(TAG, "setViewsValues: group " + m.group() + "\n");
            links.add(m.group());
            m.appendReplacement(out, "<a href=\"" + m.group() + "\">URL" + i++ + "</a>");

        }
        m.appendTail(out);
//        System.out.println(out+"!");
//
//        Log.d(TAG, "setViewsValues: HERe " + out.toString());


        textDesc.setText(Html.fromHtml(out.toString()));
        textDesc.setMovementMethod(LinkMovementMethod.getInstance());


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
        textSubmitted = (TextView) findViewById(R.id.submitted);
        textGradingStatus = (TextView) findViewById(R.id.Grading);
        textFileDownloadSize = (TextView) findViewById(R.id.textFileDownloadSize);
        iconDownloadSubmission = (ImageView) findViewById(R.id.iconDownloadSubmission);
        icDownloadAssignment = (ImageView) findViewById(R.id.icDownloadAssignment);


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

    private void rttAssignment(DatabaseReference assignmentReference) {
        Log.d(TAG, "rttAssignment: called");
        assignmentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot oneSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.equals(oneSnapshot.getKey(), incomingAssignment.getId())) {
                        try {
                            rttAssignment = oneSnapshot.getValue(Assignment.class);
                            changeViewsBasedOnStatus();
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setAssignmentDocumentCard() {
        Log.d(TAG, "setAssignmentDocumentCard: called");
    }
}