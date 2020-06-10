package com.labstechnology.project1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.labstechnology.project1.models.Quiz;
import com.labstechnology.project1.models.QuizScore;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import xyz.hasnat.sweettoast.SweetToast;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";

    private TextView textDesc, textTimeLimit, textStatus, textGrade;
    private Button btnStartQuiz;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Utils utils;
    private Quiz incomingQuiz, rttQuiz;

    private StorageReference QuizDocumentsRef, QuizDocumentsRef2;
    private DatabaseReference rttQuizDatabaseRef;
    private String id;

    //private boolean isSubmitted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        utils = new Utils(this);

        initViews();


        final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("quizzes");

        Intent intent = getIntent();
        try {
            incomingQuiz = intent.getParcelableExtra("quiz");

            id = myRef.push().getKey();

            rttQuizDatabaseRef = FirebaseDatabaseReference.DATABASE.getReference()
                    .child("quizzes").child(incomingQuiz.getId());


            QuizDocumentsRef2 = FirebaseStorage.getInstance().getReference().child("quizzesDocument").child(incomingQuiz.getId());

            setViewsValues();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle(incomingQuiz.getTitle());
        toolbar.setTitleTextColor(getColor(R.color.white1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot oneSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.equals(oneSnapshot.getKey(), incomingQuiz.getId())) {
                        try {
                            rttQuiz = oneSnapshot.getValue(Quiz.class);
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

        try {
            downloadQuizDocument(new FireBaseCallBack() {
                @Override
                public void onSuccess(Object object) {
                    try {
                        File file = (File) object;
                        Log.d(TAG, "onSuccess: FILE CONTENTS " + utils.readDocxFile(file));
                        Log.d(TAG, "onSuccess: FILE " + file.getPath());
                        Log.d(TAG, "onSuccess: FILE " + file.getName());
                        Log.d(TAG, "onSuccess: FILE" + file.getTotalSpace());
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(Object object) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void downloadQuizDocument(final FireBaseCallBack callBack) throws IOException {
        Log.d(TAG, "downloadQuizDocument: called");
        Log.d(TAG, "downloadQuizDocument: ID" + "/quizzesDocument/" + incomingQuiz.getId() + "/" + incomingQuiz.getId() + "upload");
        final StorageReference filePath = QuizDocumentsRef2.child(incomingQuiz.getId() + "upload");

        final File localFileDocx = File.createTempFile("upload", "docx");

        filePath.getFile(localFileDocx).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                SweetToast.success(QuizActivity.this, "local file has been created");
                callBack.onSuccess(localFileDocx);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });


    }

    private void changeViewsBasedOnStatus() {
        Log.d(TAG, "changeViewsBasedOnStatus: called");
    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        btnStartQuiz = (Button) findViewById(R.id.btnStartQuiz);
        textDesc = (TextView) findViewById(R.id.textDesc);
        textGrade = (TextView) findViewById(R.id.textGrade);
        textStatus = (TextView) findViewById(R.id.textStatus);
        textTimeLimit = (TextView) findViewById(R.id.textTimeLimit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @SuppressLint("SetTextI18n")
    private void setViewsValues() {
        Log.d(TAG, "setViewsValues: called");

        textDesc.setText(incomingQuiz.getDescription());
        textTimeLimit.setText(incomingQuiz.getTimeLimit()); //todo: format time limit
        if (utils.isUserAttemptThisQuiz(rttQuiz)) {
            textStatus.setText("Submitted");
            textStatus.setTextColor(getColor(R.color.green));
            btnStartQuiz.setVisibility(View.GONE);
        } else if (utils.isUserAttemptingThisQuiz(rttQuiz)) {
            textStatus.setText("Attempting");
            textStatus.setTextColor(getColor(R.color.colorWarning));
            btnStartQuiz.setText("Continue attempt");
        } else {
            textStatus.setText("Not Submitted");
            textStatus.setTextColor(getColor(R.color.colorRed));
            btnStartQuiz.setText("Start Quiz");
        }
        if (utils.isUserAttemptThisQuiz(rttQuiz)) {
            QuizScore Quizscore = utils.getQuizScore(rttQuiz);
            double score = Quizscore.getScore();
            double outOf = Quizscore.getOutOf();
            textGrade.setText(score + "/" + outOf);

        } else {
            textGrade.setText(0);
            textGrade.setTextColor(getColor(R.color.colorRed));
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}