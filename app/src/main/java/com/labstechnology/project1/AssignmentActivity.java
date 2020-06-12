package com.labstechnology.project1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.labstechnology.project1.CallBacks.FireBaseCallBack;
import com.labstechnology.project1.models.Assignment;
import com.labstechnology.project1.models.AssignmentResponse;
import com.labstechnology.project1.models.User;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.EasyPermissions;
import xyz.hasnat.sweettoast.SweetToast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AssignmentActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {
    private static final String TAG = "AssignmentActivity";


    private TextView textTitle, textDueDate, textCountDown;
    private TextView textGradingStatus, textSubmitted, textFileDownloadSize;
    private CardView cardThumbnail;
    private static final int FILE_PERMISSION_REQUEST_WRITE = 0;
    private ImageView thumbnail, thumbnailDownload, icDownloadAssignment, iconDownloadSubmission;
    private Button btnAddSubmission, btnRemoveSubmission;
    private TextView textDesc;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Assignment incomingAssignment;
    private Assignment rttAssignment;
    private boolean isSubmitted;
    private static final int FILE_PERMISSION_REQUEST_READ = 2;

    private StorageReference assignmentDocumentsRef, assignmentDocumentsRef2;
    private DatabaseReference rttAssDatabaseRef;
    private String id;

    private Utils utils;

    private String fileName, fileSize;
    private Bitmap bitmapThumbnail;
    private long dateSubmission;
    private RelativeLayout parent;
    private ProgressBar progressBar;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        initViews();
        progressBar.setVisibility(VISIBLE);

        utils = new Utils(AssignmentActivity.this);


        final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("assignments");


        Intent intent = getIntent();
        try {
            incomingAssignment = intent.getParcelableExtra("assignment");
            isSubmitted = intent.getBooleanExtra("isSubmitted", false);

            id = myRef.push().getKey();

            rttAssDatabaseRef = FirebaseDatabaseReference.DATABASE.getReference()
                    .child(FirebaseConstants.ASSIGNMENTS).child(incomingAssignment.getId());

            assignmentDocumentsRef = FirebaseStorage.getInstance().getReference("/assignmentDocumentsAdmin/-M9N8DcJ6VB-GlB10jSb");//.child("assignmentDocumentsAdmin").child(incomingAssignment.getId());


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
                try {
                    downloadSubmissionPdf(new FireBaseCallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            File file = (File) object;
                            Log.d(TAG, "onSuccess: FILE " + file.getPath());
                            Log.d(TAG, "onSuccess: FILE " + file.getName());
                            Log.d(TAG, "onSuccess: FILE" + file.getTotalSpace());
                            String size = String.valueOf(file.getTotalSpace() / 1000) + " KB";
                            textFileDownloadSize.setText(size);
                        }

                        @Override
                        public void onError(Object object) {

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        icDownloadAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(AssignmentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    try {
                        downloadAssignmentDocument(new FireBaseCallBack() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onSuccess(Object object) {
                                File file = (File) object;
                                Log.d(TAG, "onSuccess: FILE " + file.getPath());
                                Log.d(TAG, "onSuccess: FILE " + file.getName());
                                Log.d(TAG, "onSuccess: FILE" + file.getTotalSpace());
                                String size = String.valueOf(file.getTotalSpace() / 1000) + " KB";
                                textFileDownloadSize.setText(size);

                            }

                            @Override
                            public void onError(Object object) {

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    EasyPermissions.requestPermissions(AssignmentActivity.this, "we need this", FILE_PERMISSION_REQUEST_WRITE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }


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

        progressBar.setVisibility(GONE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private String getMimeType(String url) {
        String parts[] = url.split("\\.");
        String extension = parts[parts.length - 1];
        String type = null;
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }


    private void downloadAssignmentDocument(final FireBaseCallBack callBack) throws IOException {
        Log.d(TAG, "downloadAssignmentDocument: called");
        final StorageReference filePath = FirebaseStorage.getInstance().getReference("/assignmentDocumentsAdmin/" + incomingAssignment.getId() + "/" + incomingAssignment.getId() + "upload");

        // final File localFile = File.createTempFile("AssignmentUpload", "");

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        final String fname = "Assignment-" + n;

        final File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fname);
        if (!localFile.exists()) {
            localFile.createNewFile();
        }

        filePath.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                SweetToast.success(AssignmentActivity.this, "Check your Download folder for " + fname);
                icDownloadAssignment.setImageDrawable(getDrawable(R.drawable.ic_cloud_done_24px));
                callBack.onSuccess(localFile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void openFile(File url) {
        Log.d(TAG, "openFile: called");

        Uri uri = Uri.fromFile(url);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file
            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AssignmentActivity.this.startActivity(intent);

    }

    private void downloadSubmissionPdf(final FireBaseCallBack callBack) throws IOException {
        Log.d(TAG, "downloadSubmissionPdf: called");

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        final String fname = "Submission-" + n + ".pdf";

        final File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fname);
        if (!localFile.exists()) {
            localFile.createNewFile();
        }

        ArrayList<AssignmentResponse> assignmentResponses = incomingAssignment.getResponses();

        if (assignmentResponses != null) {
            for (AssignmentResponse response : assignmentResponses
            ) {
                if (response.getAssignmentId().equals(incomingAssignment.getId())) {
                    Log.d(TAG, "onSuccess: HERE" + response.getId());
                    assignmentDocumentsRef2.child(response.getId() + ".pdf").getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                SweetToast.success(AssignmentActivity.this, "Check your Download folder for " + fname);
                                iconDownloadSubmission.setImageDrawable(getDrawable(R.drawable.ic_cloud_done_24px));
                                callBack.onSuccess(localFile);
                            } else {
                                Log.d(TAG, "onComplete: failed to download submission " + task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        }


    }


    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
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
                cardThumbnail.setVisibility(VISIBLE);
                textFileDownloadSize.setVisibility(VISIBLE);
                btnRemoveSubmission.setVisibility(VISIBLE);
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
                cardThumbnail.setVisibility(VISIBLE);
                textSubmitted.setVisibility(VISIBLE);
                btnRemoveSubmission.setVisibility(VISIBLE);
                btnAddSubmission.setText("Edit submission");
                textSubmitted.setText("Submitted for grading");
                textGradingStatus.setText("Not graded");

                if (bitmapThumbnail != null) {
                    thumbnail.setImageBitmap(bitmapThumbnail);
                }

            } else {
                btnRemoveSubmission.setVisibility(View.GONE);
                textSubmitted.setVisibility(GONE);
                textGradingStatus.setVisibility(VISIBLE);

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
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        parent = (RelativeLayout) findViewById(R.id.parent);


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