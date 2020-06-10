package com.labstechnology.project1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import xyz.hasnat.sweettoast.SweetToast;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";

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
    }

    private void setViewsValues() {
        Log.d(TAG, "setViewsValues: called");
    }
}